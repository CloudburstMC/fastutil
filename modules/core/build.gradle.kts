plugins {
    id("fastutil")
}

sourceSets.main {
    java {
        srcDirs.clear()
        srcDirs(rootProject.rootDir.resolve("src"))

        include(getCoreIncludes())
    }
}

sourceSets.test {
    java {
        include(
            "$PKG/ArraysTest.java",
            "$PKG/HashCommonTest.java",
        )
    }
}

tasks.jar {
    manifest.attributes["Automatic-Module-Name"] = "fastutil.core"
}
