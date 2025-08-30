import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvmToolchain(17)

    jvm("desktop")

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
//            api(libs.square.logcat)
            // Use api to expose the dependency to dependent modules
            // Use the release variant only
//            api("io.github.oshai:kotlin-logging-android:${libs.versions.oshai.kotlin.logging.get()}")
//
//            implementation("io.github.oshai:kotlin-logging-android:${libs.versions.oshai.kotlin.logging.get()}") {
//                exclude(group = "io.github.oshai", module = "kotlin-logging")
//            }
        }

        commonMain.dependencies {
//            api(libs.square.logcat)
            // The common API is still exposed for all platforms
            api(libs.oshai.kotlin.logging)
            implementation(libs.kermit.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
//            api(libs.oshai.kotlin.logging.jvm)
//            api(libs.square.logcat)
        }
    }
}

tasks.withType<KotlinCompile>().all {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.melih.kmptemplate.shared.logging"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    kotlin {
        jvmToolchain(17)
    }
}
