plugins {
    id("fastutil")
}

dependencies {
    api(project(":modules:core"))
    for (type in Type.TYPES) {
        if (type != Type.OBJECT && type != Type.REFERENCE) {
            api(project(":modules:big-lists:$type-big-lists"))
        }
    }
}

sourceSets.main {
    java {
        srcDirs.clear()
        srcDirs(rootProject.rootDir.resolve("src"))

        include(
            "$PKG/io/*.java",
            "$PKG/**/*MappedBigList.java"
        )
    }
}

//sourceSets.test {
//    java {
//        srcDirs.clear()
//        srcDirs(rootProject.rootDir.resolve("test"))
//
//        include("$PKG/io/*.java")
//    }
//}

tasks.jar {
    manifest.attributes["Automatic-Module-Name"] = "fastutil.io"
}
