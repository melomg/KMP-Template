package com.melih.kmptemplate

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtensionConfig

@OptIn(ExperimentalWasmDsl::class)
internal fun Project.configureComposeMultiplatform(
    extension: KotlinTopLevelExtensionConfig,
) = extension.apply {
    val composeDeps = extensions.getByType<ComposePlugin.Dependencies>()

    sourceSets.apply {
        androidMain {
            dependencies {
                implementation(composeDeps.preview)
                implementation(libs.findLibrary("androidx.activity.compose").get())
            }
        }
        commonMain {
            dependencies {
                implementation(composeDeps.runtime)
                implementation(composeDeps.foundation)
                implementation(composeDeps.material3)
                implementation(composeDeps.ui)
                implementation(composeDeps.components.resources)
                implementation(composeDeps.components.uiToolingPreview)
                implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("navigation.compose").get())
            }
        }
        getByName("desktopMain").dependencies {
            implementation(composeDeps.desktop.currentOs)
            implementation(libs.findLibrary("jetbrains.kotlinx.coroutines.swing").get())
        }
    }
}
