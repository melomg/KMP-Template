plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.tmdbconfig"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.shared.logging)
            implementation(projects.core.shared.model)

            api(libs.jetbrains.kotlinx.coroutines.core)
        }
    }
}
