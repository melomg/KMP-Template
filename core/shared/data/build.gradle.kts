plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.data"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.shared.domain)
            implementation(projects.core.shared.network)
            implementation(projects.core.shared.threading)
            implementation(projects.core.shared.tmdbconfig)

            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
        }
    }
}
