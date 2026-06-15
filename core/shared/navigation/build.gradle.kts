plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.nav3Common)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.jetbrains.compose.material.icons.core)
            implementation(libs.jetbrains.lifecycle.runtimeCompose)
        }
    }
}
