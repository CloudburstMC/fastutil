subprojects {
    apply(plugin = "fastutil")

    group = "org.cloudburstmc.fastutil.queues"

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
                // Direct
                "$pkg/${cType}PriorityQueue.java",
                "$pkg/${cType}ArrayFIFOQueue.java",
                "$pkg/${cType}ArrayPriorityQueue.java",
                "$pkg/${cType}HeapPriorityQueue.java",
                "$pkg/${cType}Heaps.java",
                "$pkg/${cType}PriorityQueues.java",
                // Indirect
                "$pkg/${cType}IndirectPriorityQueue.java",
                "$pkg/${cType}ArrayIndirectPriorityQueue.java",
                "$pkg/${cType}HeapIndirectPriorityQueue.java",
                "$pkg/${cType}HeapSemiIndirectPriorityQueue.java",
                "$pkg/${cType}IndirectHeaps.java",
                "$pkg/${cType}SemiIndirectHeaps.java",
            )
        }
    }


    tasks.named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "fastutil.queues.${type.plural()}"
    }
}
