import com.melih.kmptemplate.configureComposeMultiplatform
import com.melih.kmptemplate.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// TODO: Start using this when [Issue 4082](https://github.com/JetBrains/compose-multiplatform/issues/4082) is fixed
class ComposeMultiplatformConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                configureComposeMultiplatform(this)
            }
        }
    }
}
