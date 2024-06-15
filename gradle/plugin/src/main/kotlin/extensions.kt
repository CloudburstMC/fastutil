import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.RepositoryBuilder
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

const val PKG = "it/unimi/dsi/fastutil"

fun Project.getType(): Type {
    val name = this.name.split("-")[0]

    return Type.parse(name)!!
}

fun Project.getMapTypes(): Pair<Type, Type> {
    val key = this.name.split("-")[0]
    val value = this.name.split("-")[1]

    return Pair(Type.parse(key)!!, Type.parse(value)!!)
}

fun getCoreIncludes(): Set<String> {
    val includes = mutableSetOf(
        "$PKG/Arrays.java",
        "$PKG/BigArrays.java",
        "$PKG/Function.java",
        "$PKG/Hash.java",
        "$PKG/Stack.java",
        "$PKG/HashCommon.java",
        "$PKG/SafeMath.java",
        "$PKG/BidirectionalIterator.java",
        "$PKG/PriorityQueue.java",
        "$PKG/PriorityQueues.java",
        "$PKG/IndirectPriorityQueue.java",
        "$PKG/IndirectPriorityQueues.java",
        "$PKG/BigList.java",
        "$PKG/BigListIterator.java",
        "$PKG/Size64.java",
        "$PKG/Pair.java",
        "$PKG/SortedPair.java",
        "$PKG/Swapper.java",
        "$PKG/BigSwapper.java",
        // Type specific requirements
        "$PKG/objects/ObjectBidirectionalIterator.java",
        "$PKG/objects/ObjectObjectImmutablePair.java",
        "$PKG/objects/ObjectObjectImmutableSortedPair.java",
        "$PKG/objects/ObjectIterator.java",
        "$PKG/ints/IntStack.java"
    )
    for (type in Type.TYPES) {
        val pkg = type.packagePath()
        val cType = type.capitalised()
        includes.addAll(mutableListOf(
            "$pkg/${cType}Arrays.java",
            "$pkg/${cType}BigArrays.java",
            "$pkg/${cType}Comparator.java",
            "$pkg/${cType}Comparators.java",
            "$pkg/${cType}Consumer.java",
            "$pkg/${cType}Iterator.java",
            "$pkg/${cType}Spliterator.java"
        ))
        for (mapType in Type.TYPES) {
            includes.add("$pkg/${cType}2${mapType.capitalised()}Function.java")
        }
    }
    return includes
}

fun Project.isRelease(): Boolean {
    // Find the git repository and check if the HEAD is tagged
    val repository = RepositoryBuilder()
        .findGitDir(project.rootDir)
        .setMustExist(true)
        .readEnvironment()
        .build()

    val headRef = repository.findRef("HEAD")!!
    repository.refDatabase.getRefsByPrefix(Constants.R_TAGS).forEach { ref ->
        if (ref.objectId.equals(headRef.objectId)) {
            println("Release build. Tag found for ${headRef.objectId.name}")
            return true
        }
    }
    println("Snapshot build. No tag found for ${headRef.objectId.name}")
    return false
}

fun Settings.addModule(name: String) {
    include(":modules:$name")
}