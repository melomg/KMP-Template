import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.melih.kmptemplate.ApplicationProperties

val appProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kmptemplate.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    id("com.google.gms.google-services")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.app.shared)
    implementation(compose.preview)
// TODO:   implementation(libs.compose.ui.tooling.preview)//
    implementation(libs.androidx.activity.compose)

//    implementation(libs.ktor.client.cio)
    implementation(project.dependencies.platform(libs.firebase.bom))

//    implementation(libs.androidx.navigation3.ui)
//    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.jetbrains.kotlin.test)
    testImplementation(libs.jetbrains.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

android {
    namespace = "com.melih.kmptemplate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appProperties.appIdBase
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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

    lint {
        warningsAsErrors = true
        abortOnError = false
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
