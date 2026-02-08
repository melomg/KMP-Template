package com.melih.kmptemplate

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configure base Kotlin Multiplatform
 */
@OptIn(ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) {
    extension.apply {
        jvmToolchain(17)

        // Targets
        androidTarget()

        jvm("desktop")

        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }

        wasmJs {
            nodejs()
            binaries.executable()
        }

        // Kotlin Multiplatform dependencies
        sourceSets.apply {
            commonMain {
                dependencies {
                    implementation(libs.findLibrary("koin.core").get())
                }
            }

            androidUnitTest.dependencies {
                implementation(libs.findLibrary("jetbrains.kotlinx.coroutines.core"))
                implementation(libs.findLibrary("jetbrains.kotlin.test"))
                implementation(libs.findLibrary("jetbrains.kotlin.test.junit5"))
                runtimeOnly(libs.findLibrary("junit.jupiter.engine"))
            }

            getByName("desktopMain").dependencies {
                implementation(libs.findLibrary("logback"))
            }
        }
    }
}
