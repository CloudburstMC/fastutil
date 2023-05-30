import java.util.*

val props = Properties()
file("build.properties").inputStream().use { props.load(it) }

allprojects {
    group = "org.cloudburstmc.fastutil"
    version = props.getProperty("version")
}
