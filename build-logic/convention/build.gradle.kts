import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.androidLint)
}

group = "com.melih.kmptemplate.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // For android
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.android.compose.gradlePlugin)

    // For KMP
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)

    compileOnly(libs.ksp.gradlePlugin)
    lintChecks(libs.androidx.lint.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = libs.plugins.kmptemplate.kotlinMultiplatform.library.get().pluginId
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
        register("kmpComposeMultiplatform") {
            id = libs.plugins.kmptemplate.compose.multiplatform.get().pluginId
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.kmptemplate.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLint") {
            id = libs.plugins.kmptemplate.android.lint.get().pluginId
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("applicationProperties") {
            id = libs.plugins.kmptemplate.application.properties.get().pluginId
            implementationClass = "ApplicationPropertiesConventionPlugin"
        }
    }
}
