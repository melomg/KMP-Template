package com.melih.kmptemplate

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) = extension.apply {
    jvmToolchain(17)

    android {
        compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
        minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    wasmJs {
        nodejs()
        binaries.executable()
    }

    sourceSets.apply {
        getByName("desktopMain").dependencies {
            implementation(libs.findLibrary("jetbrains-kotlinx-coroutines-swing").get())
            implementation(libs.findLibrary("logback").get())
        }

        commonMain {
            dependencies {
                implementation(libs.findLibrary("koin.core").get())
            }
        }

        androidUnitTest.dependencies {
            implementation(libs.findLibrary("jetbrains.kotlinx.coroutines.core").get())
            implementation(libs.findLibrary("jetbrains.kotlin.test").get())
            implementation(libs.findLibrary("jetbrains.kotlin.test.junit5").get())
            runtimeOnly(libs.findLibrary("junit.jupiter.engine").get())
        }
    }
}

private fun KotlinMultiplatformExtension.android(
    configure: KotlinMultiplatformAndroidLibraryTarget.() -> Unit,
) {
    (this as ExtensionAware).extensions.configure("android", configure)
}
