plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.testutils"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.shared.dataTestUtils)
            implementation(projects.core.shared.model)
            implementation(projects.core.shared.threading)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
            implementation(libs.testBalloon.framework.core)
        }
    }
}
