description = "Fastutil BOM"

plugins {
    id("java-platform")
    id("fastutil.base")
}

dependencies {
    constraints {
        gradle.projectsEvaluated {
            for (subProject in project.rootProject.subprojects) {
                if (subProject != project && subProject.plugins.hasPlugin(MavenPublishPlugin::class.java)) {
                    api(subProject)
                }
            }
        }
    }
}

configure<PublishingExtension> {
    publications {
        named<MavenPublication>("maven") {
            from(components["javaPlatform"])
            pom {
                packaging = "pom"
            }
        }
    }
}