import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.melih.kmptemplate.ApplicationProperties

val appProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kmptemplate.application.properties)
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.melih.kmptemplate.shared"
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeIOSApp"
            isStatic = true
        }
    }

    // TODO: check if necessary to apply
    //  applyDefaultHierarchyTemplate()

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.ktor.client.cio)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        commonMain.dependencies {
            api(projects.core.shared.logging)
            api(projects.core.shared.model)
            api(projects.core.shared.network)
            api(projects.core.shared.resources)
            api(projects.core.shared.threading)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.jetbrains.compose.material.icons.core)
            implementation(libs.jetbrains.lifecycle.runtimeCompose)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.uiTooling)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

buildkonfig {
    packageName = "com.melih.kmptemplate"

    defaultConfigs {
        buildConfigField(STRING, "APP_NAME", appProperties.appName)
        buildConfigField(STRING, "EFFECTIVE_BUILD_TYPE", appProperties.effectiveBuildType)
        buildConfigField(INT, "VERSION_CODE", appProperties.versionCode.toString())
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName)
        buildConfigField(
            BOOLEAN, "IS_DEBUGGABLE", appProperties.isDebuggable.toString()
        )
    }
}
