plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.data.testfixtures"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.shared.modelTestfixtures)
            implementation(projects.core.shared.data)
            implementation(projects.core.shared.domain)
            implementation(projects.core.shared.threading)
            implementation(libs.jetbrains.kotlin.test)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
            implementation(libs.koin.test)
            implementation(libs.testBalloon.framework.core)
        }
    }
}
