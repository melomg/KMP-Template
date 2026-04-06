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
            implementation(libs.jetbrains.kotlinx.coroutines.android)
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

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
