import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kmptemplate.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.melih.kmptemplate.core.shared.network"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        commonMain.dependencies {
            api(projects.core.shared.logging)
            api(projects.core.shared.model)

            implementation(libs.koin.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        commonTest.dependencies {
            implementation(libs.jetbrains.kotlin.test)
        }
    }
}
