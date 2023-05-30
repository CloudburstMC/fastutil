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
}

fun addModule(name: String) {
    include(":modules:$name")
}


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

