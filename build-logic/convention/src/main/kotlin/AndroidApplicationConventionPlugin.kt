import com.android.build.api.dsl.ApplicationExtension
import com.melih.kmptemplate.configureKotlinAndroid
import com.melih.kmptemplate.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidApplication").get().get().pluginId)
                apply(libs.findPlugin("kmptemplate-android-lint").get().get().pluginId)
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk =
                    libs.findVersion("android-targetSdk").get().toString().toInt()

                testOptions.animationsDisabled = true

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}
