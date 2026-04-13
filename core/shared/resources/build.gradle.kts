plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.resources"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.melih.kmptemplate.core.shared.resources"
    generateResClass = always
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
