plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.features.museum"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.shared.designsystem)
            implementation(projects.core.shared.domain)
            implementation(projects.core.shared.l10n)
            implementation(projects.core.shared.model)
            implementation(projects.core.shared.navigation)
            implementation(projects.core.shared.threading)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.jetbrains.compose.material.icons.core)
            implementation(libs.jetbrains.lifecycle.runtimeCompose)

            implementation(libs.coil.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.uiTooling)
}
