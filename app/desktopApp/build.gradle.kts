import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.melih.kmptemplate.ApplicationProperties

val appProperties: ApplicationProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kmptemplate.application.properties)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

dependencies {
    implementation(projects.app.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.jetbrains.kotlinx.coroutines.swing)
    implementation(libs.ktor.client.cio)
    implementation(libs.logback)
}

compose.desktop {
    application {
        mainClass = "com.melih.kmptemplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appProperties.appIdBase
            packageVersion = appProperties.versionNameBase

            // TODO Template: Change app icons
            macOS {
                iconFile.set(project.file("src/main/composeResources/app-icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/composeResources/app-icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/composeResources/app-icon.png"))
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
