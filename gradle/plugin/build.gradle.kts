plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.10.0.202406032230-r")
}

gradlePlugin {
    val fastutilPlugin by plugins.creating {
        id = "org.cloudburstmc.fastutil-plugin"
        implementationClass = "FastutilSettingsPlugin"
        displayName = "Fastutil plugin"
        description = "A no-op plugin to expose classes from the plugin"
        tags.set(listOf("gradle", "fastutil"))
    }
}
