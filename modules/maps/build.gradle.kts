subprojects {
    apply(plugin = "fastutil")

    val (key, value) = project.getMapTypes()

    val pkg = key.packagePath()
    val cType = key.capitalised()
    val valCType = value.capitalised()

    dependencies {
        "api"(project(":modules:sets:object-sets"))
        "api"(project(":modules:sets:$key-sets"))
        "api"(project(":modules:sets:$value-sets"))
    }

    configure<SourceSetContainer> {
        get("main").java {
            srcDirs.clear()
            srcDirs(rootProject.rootDir.resolve("src"))

            include(
                "$pkg/Abstract${cType}2${valCType}*Map.java",
                "$pkg/Abstract${cType}2${valCType}*Function.java",
                "$pkg/${cType}2${valCType}*Map.java",
                "$pkg/${cType}2${valCType}*Maps.java",
                "$pkg/${cType}2${valCType}*Functions.java",
            )
        }
    }


    tasks.named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "fastutil.maps.${key.plural()}.${value.plural()}"
    }
}
