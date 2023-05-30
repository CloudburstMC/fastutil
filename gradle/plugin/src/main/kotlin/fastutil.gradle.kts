plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

publishing {
    repositories {
        maven {
            name = "maven-deploy"
            url = uri(
                System.getenv("MAVEN_DEPLOY_URL") ?: "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            )
            credentials {
                username = System.getenv("MAVEN_DEPLOY_USERNAME") ?: "username"
                password = System.getenv("MAVEN_DEPLOY_PASSWORD") ?: "password"
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            pom {
                packaging = "jar"
                url.set("https://github.com/CloudburstMC/fastutil")

                scm {
                    connection.set("scm:git:git://github.com/CloudburstMC/fastutil.git")
                    developerConnection.set("scm:git:ssh://github.com/CloudburstMC/fastutil.git")
                    url.set("https://github.com/CloudburstMC/fastutil")
                }

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("CloudburstMC Team")
                        organization.set("CloudburstMC")
                        organizationUrl.set("https://github.com/CloudburstMC")
                    }
                }
            }
        }
    }
}

signing {
    if (System.getenv("PGP_SECRET") != null && System.getenv("PGP_PASSPHRASE") != null) {
        useInMemoryPgpKeys(System.getenv("PGP_SECRET"), System.getenv("PGP_PASSPHRASE"))
        sign(publishing.publications["maven"])
    }
}

tasks.javadoc {
    options {
        (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name();
}
