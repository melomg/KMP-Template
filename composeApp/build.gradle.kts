import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

/**
 * Gather build flags in one central place.
 */
class BuildFlags(project: Project) {

// Reads the Google maps key that is used in the AndroidManifest

    private val applicationProperties = Properties()
    private val applicationPropertiesFile = project.rootProject.file("application.properties")

    init {
        if (applicationPropertiesFile.exists()) {
            applicationPropertiesFile.inputStream().use { input ->
                applicationProperties.load(input)
            }
        }
    }

    val isChuckerEnabled = project.findProperty("chuckerEnabled") == "true"
    val isLeakCanaryEnabled = project.findProperty("leakCanaryEnabled") == "true"
    val isCI = System.getenv().containsKey("CI")
    val isPlayStorePublishDryRun = System.getenv().containsKey("PLAY_STORE_PUBLISH_DRY_RUN")
    val isTestCoverageEnabled = applicationProperties.getProperty("isTestCoverageEnabled") == "true"
    val preReleaseResConfig = project.findProperty("preReleaseResConfig") == "true"
}

val buildFlags = BuildFlags(project)

//val localProperties = Properties()
//if (project.rootProject.file("local.properties").exists()) {
//    properties.load(rootProject.file("local.properties").newDataInputStream())
//}
//// Getting The Movie DB API key from local.properties
//val tmdbApiKey = gradleLocalProperties(rootDir).getProperty("tmdb_api_key")
//
//def localProperties = new Properties()
//def localPropertiesFile = rootProject.file('local.properties')
//if (localPropertiesFile.exists()) {
//    localPropertiesFile.withReader('UTF-8') { reader ->
//        localProperties.load(reader)
//    }
//}
//
//def flutterRoot = localProperties.getProperty('flutter.sdk')


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.cio)

            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(projects.shared.model)
            implementation(projects.shared.network)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.jetbrains.material.icons.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.jetbrains.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

android {
    namespace = "com.melih.kmptemplate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.melih.kmptemplate"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"
        resValue("string", "app_name", "KMP Template")
    }

    signingConfigs {
        val localProperties: java.util.Properties = gradleLocalProperties(rootDir, providers)
        val localStoreFile: String = localProperties.getProperty("androidReleaseStoreFile", ".")
        val localStorePassword: String =
            localProperties.getProperty("androidReleaseStorePassword", "")
        val localKeyAlias: String = localProperties.getProperty("androidReleaseKeyAlias", "")
        val localKeyPassword: String = localProperties.getProperty("androidReleaseKeyPassword", "")

        create("prod") {
            // storeFile = file("../tools/release.jks")
            storeFile = file(localStoreFile).relativeToOrSelf(projectDir)
            storePassword = localStorePassword//project.storePassword// localStorePassword
            keyAlias = localKeyAlias//localKeyAlias
            keyPassword = localKeyPassword//project.keyPassword// localKeyPassword
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            resValue("string", "app_name", "Debug KMP Template")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                // Default file with automatically generated optimization rules.
                getDefaultProguardFile("proguard-android-optimize.txt")
            )

            signingConfig = signingConfigs.getByName("prod")
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-STAGING"

            resValue("string", "app_name", "Staging KMP Template")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    bundle {
        language {
            // Include all languages in app bundles
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    if (buildFlags.isTestCoverageEnabled) {
        println("melo: Test coverage enabled")
    }else {
        println("melo: ${project.findProperty("isTestCoverageEnabled")}")
        println("melo: Test coverage DISABLED")
    }
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.melih.kmptemplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.melih.kmptemplate"
            packageVersion = "1.0.0"
        }
    }
}


val appBuildTypeFromProperty =
    project.findProperty("appBuildType")?.toString()?.toLowerCase() ?: "debug"
// You can uncomment the line below to verify which build type is being used during configuration:
// println("BuildKonfig: Configuring for appBuildType = '$appBuildTypeFromProperty'")


buildkonfig {
    packageName = "com.melih.kmptemplate" // Your existing package name

    defaultConfigs {
        // Your existing API_KEY logic
        val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("apiKey")
        require(apiKey.isNotEmpty()) {
            "Register your api key from developer.nytimes.com and place it in local.properties as `apiKey`"
        }
        // Ensure the apiKey value passed to buildConfigField is a Kotlin string literal
        buildConfigField(STRING, "API_KEY", "$apiKey")
        buildConfigField(STRING, "name", project.name)
        buildConfigField(STRING, "version", provider { project.version }.toString())
        buildConfigField(BOOLEAN, "isDebuggable", "true")

        // Add build-type specific values
        val featureFlagEnabled: Boolean // Example of a boolean flag

        when (appBuildTypeFromProperty) {
            "release" -> {
                featureFlagEnabled = true // Booleans don't need quotes
                // Add any other release-specific fields here
                buildConfigField(STRING, "ENVIRONMENT_NAME", "production")
            }

            "staging" -> {
                featureFlagEnabled = true
                buildConfigField(STRING, "ENVIRONMENT_NAME", "staging")
            }

            else -> { // "debug" or any other default
                featureFlagEnabled = false
                buildConfigField(STRING, "ENVIRONMENT_NAME", "development")
            }
        }
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN,
            "MY_FEATURE_FLAG",
            featureFlagEnabled.toString()
        )
        buildConfigField(
            STRING,
            "EFFECTIVE_BUILD_TYPE",
            "$appBuildTypeFromProperty"
        ) // Store the active build type name
    }

    // Your existing targetConfigs can remain as they are for platform-specific (not build-type specific) overrides
    targetConfigs {
        create("jvm") {
            buildConfigField(
                FieldSpec.Type.STRING,
                "target",
                "jvm"
            ) // Ensure string values are quoted
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "target", "ios")
        }
        create("desktop") { // This should align with your jvm("desktop") target
            buildConfigField(FieldSpec.Type.STRING, "desktopvalue", "desktop")
        }
        create("jsCommon") { // This should align with your wasmJs target (or plain js if you had one)
            buildConfigField(FieldSpec.Type.STRING, "target", "jsCommon")
        }
    }
}
