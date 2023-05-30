subprojects {
    apply(plugin = "fastutil")

    val type = project.getType()
    val pkg = type.packagePath()
    val cType = type.capitalised()

    dependencies {
        "api"(project(":modules:commons:$type-common"))
        if (type.parent() == Type.OBJECT) {
            "api"(project(":modules:big-lists:${type.parent()}-big-lists"))
        }
    }

    configure<SourceSetContainer> {
        get("main").java {
            srcDirs.clear()
            srcDirs(rootProject.rootDir.resolve("src"))

            include(
                "$pkg/${cType}MappedBigList.java",
                "$pkg/Abstract${cType}BigList.java",
                "$pkg/${cType}BigList.java",
                "$pkg/${cType}BigSpliterators.java",
                "$pkg/${cType}BigListIterators.java"
            )
        }
    }


    tasks.named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "fastutil.big-lists.${type.plural()}"
    }
}
