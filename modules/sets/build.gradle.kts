subprojects {
    apply(plugin = "fastutil")

    val type = project.getType()
    val pkg = type.packagePath()
    val cType = type.capitalised()

    dependencies {
        "api"(project(":modules:commons:$type-common"))
    }

    configure<SourceSetContainer> {
        get("main").java {
            srcDirs.clear()
            srcDirs(rootProject.rootDir.resolve("src"))

            include(
                // Unsorted
                "$pkg/${cType}Set.java",
                "$pkg/${cType}Sets.java",
                "$pkg/Abstract${cType}Set.java",
                "$pkg/${cType}ArraySet.java",
                "$pkg/${cType}OpenCustomHashSet.java",
                "$pkg/${cType}OpenHashSet.java",
                // Sorted
                "$pkg/${cType}SortedSet.java",
                "$pkg/${cType}SortedSets.java",
                "$pkg/Abstract${cType}SortedSet.java",
                "$pkg/${cType}AVLTreeSet.java",
                "$pkg/${cType}LinkedOpenCustomHashSet.java",
                "$pkg/${cType}LinkedOpenHashSet.java",
                "$pkg/${cType}RBTreeSet.java"
            )
        }
    }


    tasks.named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "fastutil.sets.${type.plural()}"
    }
}
