import java.util.*

val props = Properties()
file("build.properties").inputStream().use { props.load(it) }

val fastUtilVersion = props.getProperty("version") + if (isRelease()) "" else "-SNAPSHOT"

allprojects {
    group = "org.cloudburstmc.fastutil"
    version = fastUtilVersion
}
