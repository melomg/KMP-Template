import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.melih.kmptemplate.ApplicationProperties

val appProperties: ApplicationProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kmptemplate.application.properties)
    alias(libs.plugins.kmptemplate.android.application)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
}

dependencies {
    implementation(projects.app.shared)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.jetbrains.compose.foundation)

    implementation(project.dependencies.platform(libs.firebase.bom))

    testImplementation(libs.jetbrains.kotlin.test)
    testImplementation(libs.jetbrains.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

android {
    namespace = "com.melih.kmptemplate"

    defaultConfig {
        applicationId = appProperties.appIdBase
        versionCode = appProperties.versionCode
        versionName = appProperties.versionNameBase
        resValue("string", "app_name", appProperties.appName)
    }

    signingConfigs {
        val localProperties = gradleLocalProperties(rootDir, providers)
        create("prod") {
            storeFile = file("../androidApp/release.jks")
            storePassword = localProperties.getProperty("storePassword")
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
        }
    }

    buildFeatures {
        buildConfig = true
        resValues = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = appProperties.appIdDebugSuffix
            versionNameSuffix = appProperties.versionNameDebugSuffix
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            signingConfig = signingConfigs.getByName("prod")
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = appProperties.appIdStagingSuffix
            versionNameSuffix = appProperties.versionNameStagingSuffix

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    bundle {
        language {
            // Include all languages in app bundles
            enableSplit = false
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
