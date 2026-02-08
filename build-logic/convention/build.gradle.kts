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
    compileOnly(libs.android.gradlePlugin) // If targeting Android
    compileOnly(libs.android.tools.common)
    compileOnly(libs.android.compose.gradlePlugin)

    // For KMP
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)

//    compileOnly(libs.firebase.crashlytics.gradlePlugin)
//    compileOnly(libs.firebase.performance.gradlePlugin)
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
            id = libs.plugins.kmptemplate.kmp.library.get().pluginId
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
        register("kmpComposeMultiplatform") {
            id = libs.plugins.kmptemplate.compose.multiplatform.get().pluginId
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        //        register("androidApplicationCompose") {
//            id = libs.plugins.kmptemplate.android.application.compose.get().pluginId
//            implementationClass = "AndroidApplicationComposeConventionPlugin"
//        }
        register("androidApplication") {
            id = libs.plugins.kmptemplate.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
//        register("androidApplicationJacoco") {
//            id = libs.plugins.kmptemplate.android.application.jacoco.get().pluginId
//            implementationClass = "AndroidApplicationJacocoConventionPlugin"
//        }
//        register("androidLibraryCompose") {
//            id = libs.plugins.kmptemplate.android.library.compose.get().pluginId
//            implementationClass = "AndroidLibraryComposeConventionPlugin"
//        }
//        register("androidLibrary") {
//            id = libs.plugins.kmptemplate.android.library.asProvider().get().pluginId
//            implementationClass = "AndroidLibraryConventionPlugin"
//        }
//        register("androidFeatureImpl") {
//            id = libs.plugins.kmptemplate.android.feature.impl.get().pluginId
//            implementationClass = "AndroidFeatureImplConventionPlugin"
//        }
//        register("androidFeatureApi") {
//            id = libs.plugins.kmptemplate.android.feature.api.get().pluginId
//            implementationClass = "AndroidFeatureApiConventionPlugin"
//        }
//        register("androidLibraryJacoco") {
//            id = libs.plugins.kmptemplate.android.library.jacoco.get().pluginId
//            implementationClass = "AndroidLibraryJacocoConventionPlugin"
//        }
        register("androidTest") {
            id = libs.plugins.kmptemplate.android.test.get().pluginId
            implementationClass = "AndroidTestConventionPlugin"
        }
//        register("androidFlavors") {
//            id = libs.plugins.kmptemplate.android.application.flavors.get().pluginId
//            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
//        }
        register("androidLint") {
            id = libs.plugins.kmptemplate.android.lint.get().pluginId
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.kmptemplate.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
