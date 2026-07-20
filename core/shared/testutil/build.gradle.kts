plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.testutil"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.shared.model)
            implementation(projects.core.shared.threading)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
            implementation(libs.testBalloon.framework.core)
        }
    }
}
