plugins {
    id("fastutil.base")
    id("java-library")
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

publishing {
    publications {
        named<MavenPublication>("maven") {
            from(components["java"])
            pom {
                packaging = "jar"
            }
        }
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