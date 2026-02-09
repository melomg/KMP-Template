plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compose.desktop {
        application {
            mainClass = "com.melih.kmptemplate.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = appProperties.appIdBase
                packageVersion = appProperties.versionNameBase
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.shared)
        }
    }
}
