import com.melih.kmptemplate.configureKotlinMultiplatform
import com.melih.kmptemplate.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
//                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            }

//            extensions.configure<LibraryExtension> {
////                configureKotlinAndroid(this)
//            }
            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(this)
            }
        }
    }
}
