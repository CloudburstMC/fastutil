import java.util.*

val props = Properties()
file("build.properties").inputStream().use { props.load(it) }

val release: String by properties

allprojects {
    group = "org.cloudburstmc.fastutil"
    version = props.getProperty("version") + if(release.toBoolean()) "" else "-SNAPSHOT"
}
