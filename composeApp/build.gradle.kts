import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

val appProperties = ApplicationProperties(project)

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
    namespace = appProperties.appId
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appProperties.appId
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appProperties.versionCode
        versionName = appProperties.versionName
        resValue("string", "app_name", appProperties.appName)
    }

    signingConfigs {
        create("prod") {
            storeFile = file("../tools/release.jks")
            storePassword = "..."
            keyAlias = "..."
            keyPassword = "..."
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

            resValue("string", "app_name", "${appProperties.appName} (Debug)")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt")
            )

            signingConfig = signingConfigs.getByName("prod")
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-STAGING"

            resValue("string", "app_name", "${appProperties.appName} (Staging)")
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

    lint {
        warningsAsErrors = true
        abortOnError = appProperties.isCI
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.melih.kmptemplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appProperties.appId
            packageVersion = appProperties.versionName
        }
    }
}

class ApplicationProperties(project: Project) {

    private val properties = Properties()

    init {
        val propertiesFile = project.rootProject.file("application.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        }
    }

    val isCI: Boolean
        get() = System.getenv().containsKey("CI")

    val appId: String
        get() = properties.getProperty("app.id")
    val appName: String
        get() = properties.getProperty("app.name")
    val versionCode: Int
        get() = properties.getProperty("app.versionCode").toInt()
    val versionName: String
        get() = properties.getProperty("app.versionName")
    val buildType: String
        get() = properties.getProperty("app.buildType")
    val isDebuggable: Boolean
        get() = properties.getProperty("app.isDebuggable") == "true"

    val androidReleaseStoreFilePath: String
        get() = properties.getProperty("signing.android.release.storeFilePath")
    val androidReleaseStorePassword: String
        get() = properties.getProperty("signing.android.release.storePassword")
    val androidReleaseKeyAlias: String
        get() = properties.getProperty("signing.android.release.keyAlias")
    val androidReleaseKeyPassword: String
        get() = properties.getProperty("signing.android.release.keyPassword")

    val isTestCoverageEnabled: Boolean
        get() = properties.getProperty("isTestCoverageEnabled") == "true"
}

buildkonfig {
    packageName = "com.melih.kmptemplate" // Your existing package name

    defaultConfigs {
        // Your existing API_KEY logic
//        val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("apiKey")
//        require(apiKey.isNotEmpty()) {
//            "Register your api key from developer.nytimes.com and place it in local.properties as `apiKey`"
//        }
//        // Ensure the apiKey value passed to buildConfigField is a Kotlin string literal
//        buildConfigField(STRING, "API_KEY", "$apiKey")
//        buildConfigField(STRING, "name", project.name)
//        buildConfigField(STRING, "version", provider { project.version }.toString())
        buildConfigField(INT, "VERSION_CODE", appProperties.versionCode.toString())
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName)
        buildConfigField(STRING, "BUILD_TYPE", appProperties.buildType)
        buildConfigField(BOOLEAN, "isDebuggable", appProperties.isDebuggable.toString())
        buildConfigField(STRING, "APP_NAME", appProperties.appName)

        // Add build-type specific values
//        val featureFlagEnabled: Boolean // Example of a boolean flag
//
//        when (appBuildTypeFromProperty) {
//            "release" -> {
//                featureFlagEnabled = true // Booleans don't need quotes
//                // Add any other release-specific fields here
//                buildConfigField(STRING, "ENVIRONMENT_NAME", "production")
//            }
//
//            "staging" -> {
//                featureFlagEnabled = true
//                buildConfigField(STRING, "ENVIRONMENT_NAME", "staging")
//            }
//
//            else -> { // "debug" or any other default
//                featureFlagEnabled = false
//                buildConfigField(STRING, "ENVIRONMENT_NAME", "development")
//            }
//        }
//        buildConfigField(
//            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN,
//            "MY_FEATURE_FLAG",
//            featureFlagEnabled.toString()
//        )
//        buildConfigField(
//            STRING,
//            "EFFECTIVE_BUILD_TYPE",
//            "$appBuildTypeFromProperty"
//        ) // Store the active build type name
    }

    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("devDebug") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "dev")
        buildConfigField(STRING, "isDebuggable", "true")
    }
    defaultConfigs("devRelease") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "dev") // BuildFlavor??
        buildConfigField(STRING, "isDebuggable", "false")
    }
    defaultConfigs("stagingDebug") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "staging") // BuildFlavor??
        buildConfigField(STRING, "isDebuggable", "true")
    }
    defaultConfigs("stagingRelease") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "staging") // BuildFlavor??
        buildConfigField(STRING, "isDebuggable", "true")
    }
    defaultConfigs("prodDebug") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "prod") // BuildFlavor??
        buildConfigField(STRING, "isDebuggable", "true")
    }
    defaultConfigs("prodRelease") {
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName+"-devDebug")
        buildConfigField(STRING, "BUILD_TYPE", "prod") // BuildFlavor??
        buildConfigField(STRING, "isDebuggable", "true")
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
            val desktopPackageName =
                (project.extensions.findByName("compose") as? org.jetbrains.compose.ComposeExtension)
                    ?.desktop?.application?.nativeDistributions?.packageName
                    ?: "com.example.defaultdesktop"
            buildConfigField(STRING, "PACKAGE_NAME", desktopPackageName)
        }
        create("jsCommon") { // This should align with your wasmJs target (or plain js if you had one)
            buildConfigField(FieldSpec.Type.STRING, "target", "jsCommon")
        }
    }
}
