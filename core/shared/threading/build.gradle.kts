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

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
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
        val androidUnitTest by getting
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.jetbrains.kotlinx.coroutines.android)
        }
        androidUnitTest.dependencies {
            implementation(libs.jetbrains.kotlinx.coroutines.test)
            implementation(libs.jetbrains.kotlin.test)
            implementation(libs.jetbrains.kotlin.test.junit5)
            runtimeOnly(libs.junit.jupiter.engine)
        }

        desktopMain.dependencies {
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }

        commonMain.dependencies {
            api(projects.core.shared.logging)
            api(projects.core.shared.model)

            implementation(libs.jetbrains.kotlinx.coroutines.core)
        }
    }
}

tasks.withType<KotlinCompile>().all {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

android {
    namespace = "com.melih.kmptemplate.core.shared.threading"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    kotlin {
        jvmToolchain(17)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources.excludes += "DebugProbesKt.bin"
    }
}
