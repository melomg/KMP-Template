plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.threading"

        packaging {
            resources.excludes += "DebugProbesKt.bin"
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            api(libs.jetbrains.kotlinx.coroutines.android)
        }

        desktopMain.dependencies {
            api(libs.jetbrains.kotlinx.coroutines.swing)
        }

        commonMain.dependencies {
            implementation(projects.core.shared.logging)
            implementation(projects.core.shared.model)

            api(libs.jetbrains.kotlinx.coroutines.core)
        }
    }
}
