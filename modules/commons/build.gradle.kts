subprojects {
    apply(plugin = "fastutil")

    group = "org.cloudburstmc.fastutil.commons"

    val type = project.getType()
    val pkg = type.packagePath()
    val cType = type.capitalised()

    dependencies {
        "api"(project(":modules:core"))
        if (type.parent() != null) {
            "api"(project(":modules:commons:${type.parent()}-common"))
        }
    }

    configure<SourceSetContainer> {
        get("main").java {
            srcDirs.clear()
            srcDirs(rootProject.rootDir.resolve("src"))

            include(
                "$pkg/${cType}BidirectionalIterable.java",
                "$pkg/${cType}BinaryOperator.java",
                "$pkg/${cType}Collection.java",
                "$pkg/${cType}Collections.java",
                "$pkg/Abstract${cType}Collection.java",
                "$pkg/Abstract${cType}Iterator.java",
                "$pkg/${cType}Hash.java",
                "$pkg/${cType}Iterators.java",
                "$pkg/${cType}Iterable.java",
                "$pkg/${cType}ListIterator.java",
                "$pkg/${cType}List.java",
                "$pkg/${cType}Predicate.java",
                "$pkg/${cType}*Pair.java",
                "$pkg/${cType}Spliterators.java",
                "$pkg/${cType}UnaryOperator.java",
                "$pkg/Abstract${cType}Spliterator.java",
                "$pkg/${cType}ArrayList.java",
                "$pkg/Abstract${cType}List.java",
                "$pkg/${cType}BigListIterator.java",
                "$pkg/${cType}ImmutableList.java",
                "$pkg/${cType}Lists.java",
                "$pkg/${cType}BidirectionalIterator.java",
                "$pkg/${cType}Iterator.java",
                "$pkg/${cType}Stack.java",
            )

            includes.removeAll(getCoreIncludes())
        }
    }

    tasks.named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "fastutil.commons.${type.plural()}"
    }
}
