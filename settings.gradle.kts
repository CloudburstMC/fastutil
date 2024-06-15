pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
    includeBuild("gradle/plugin")
    plugins {
        id("fastutil")
    }
}

plugins {
    id("org.cloudburstmc.fastutil-plugin")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

addModule("bom")
addModule("core")
addModule("io")

for (type in Type.TYPES) {
    addModule("commons:$type-common")
    if (type != Type.REFERENCE && type != Type.BOOLEAN) { // No reference queue implementation
        addModule("queues:$type-queues")
    }
    addModule("sets:$type-sets")
    addModule("big-lists:$type-big-lists")

    if (type != Type.BOOLEAN) {
        for (mapType in Type.TYPES) {
            addModule("maps:$type-$mapType-maps")
        }
    }
}

