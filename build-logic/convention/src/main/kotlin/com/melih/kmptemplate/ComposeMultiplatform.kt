package com.melih.kmptemplate

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalWasmDsl::class)
internal fun Project.configureComposeMultiplatform(
    extension: KotlinMultiplatformExtension,
) = extension.apply {
    // TODO remove compose deps
    val composeDeps = extensions.getByType<ComposePlugin.Dependencies>()

    sourceSets.apply {
        androidMain {
            dependencies {
                implementation(libs.findLibrary("jetbrains-compose-uiToolingPreview").get())
                implementation(libs.findLibrary("androidx.activity.compose").get())
            }
        }

        getByName("desktopMain").dependencies {
            implementation(composeDeps.desktop.currentOs)
            implementation(libs.findLibrary("jetbrains.kotlinx.coroutines.swing").get())
        }

        commonMain {
            dependencies {
                implementation(libs.findLibrary("jetbrains-compose-components-resources").get())
                implementation(libs.findLibrary("jetbrains-compose-foundation").get())
                implementation(libs.findLibrary("jetbrains-compose-material-icons-core").get())
                implementation(libs.findLibrary("jetbrains-compose-runtime").get())
                implementation(libs.findLibrary("jetbrains-compose-ui").get())
                implementation(libs.findLibrary("jetbrains-compose-uiToolingPreview").get())
                implementation(libs.findLibrary("jetbrains-compose-navigation").get())

                implementation(libs.findLibrary("jetbrains-lifecycle-runtimeCompose").get())
                implementation(libs.findLibrary("jetbrains.lifecycle.viewmodel").get())
            }
        }
    }
}
