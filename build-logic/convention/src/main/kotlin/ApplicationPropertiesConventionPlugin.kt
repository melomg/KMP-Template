import org.gradle.api.Plugin
import org.gradle.api.Project

class ApplicationPropertiesConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // no-op, this is necessary just for applying application properties.
    }
}
