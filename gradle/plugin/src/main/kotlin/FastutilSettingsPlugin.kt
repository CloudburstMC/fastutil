import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Suppress("unused")
abstract class FastutilSettingsPlugin: Plugin<Settings> {

    override fun apply(settings: Settings) {
        // no-op
    }
}
