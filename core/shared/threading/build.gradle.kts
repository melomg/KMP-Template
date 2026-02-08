import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kmptemplate.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.melih.kmptemplate.core.shared.threading"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions.jvmTarget = JvmTarget.JVM_17

        packaging {
            resources.excludes += "DebugProbesKt.bin"
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
//        val androidUnitTest by getting
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.jetbrains.kotlinx.coroutines.android)
        }
        // TODO
//        androidUnitTest.dependencies {
//            implementation(libs.jetbrains.kotlinx.coroutines.test)
//            implementation(libs.jetbrains.kotlin.test)
//            implementation(libs.jetbrains.kotlin.test.junit5)
//            runtimeOnly(libs.junit.jupiter.engine)
//        }

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

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
