import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
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
    alias(libs.plugins.sentry)
    id("com.google.gms.google-services")

//    id("io.sentry.android.gradle") version "5.9.0"
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
            implementation(projects.shared.logging)
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
            storeFile = file("../tools/release.jks")
            storePassword = localProperties.getProperty("storePassword")
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
        }
    }

    buildFeatures {
        buildConfig = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

dependencies {
    debugImplementation(compose.uiTooling)
}

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
        buildConfigField(STRING, "SENTRY_DSN", appProperties.sentryDSN)
    }
}

class ApplicationProperties(project: Project) {

    val effectiveBuildType: String = project.effectiveBuildType()
    private val properties = Properties()
    private val propertiesFile: File = project.rootProject.file("application.properties")

    init {
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        }
    }

    val isCI: Boolean
        get() = System.getenv().containsKey("CI")

    val appIdBase: String
        get() = properties.getProperty("appIdBase")

    val appIdDebugSuffix: String
        get() = properties.getProperty("appIdDebugSuffix")

    val appIdStagingSuffix: String
        get() = properties.getProperty("appIdStagingSuffix")

    val appName: String
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> appNameBase + properties.getProperty("appNameDebugSuffix")
            AppBuildType.STAGING -> appNameBase + properties.getProperty("appNameStagingSuffix")
            AppBuildType.RELEASE -> appNameBase
        }

    private val appNameBase: String
        get() = properties.getProperty("appNameBase")

    val versionCode: Int
        get() = properties.getProperty("versionCode").toInt()

    val versionName: String
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> versionNameBase + versionNameDebugSuffix
            AppBuildType.STAGING -> versionNameBase + versionNameStagingSuffix
            AppBuildType.RELEASE -> versionNameBase
        }

    val versionNameBase: String
        get() = properties.getProperty("versionNameBase")

    val versionNameDebugSuffix: String
        get() = properties.getProperty("versionNameDebugSuffix")

    val versionNameStagingSuffix: String
        get() = properties.getProperty("versionNameStagingSuffix")

    val isDebuggable: Boolean
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> true
            AppBuildType.STAGING -> false
            AppBuildType.RELEASE -> false
        }

    val sentryDSN: String
        get() = gradleLocalProperties(rootDir, providers).getProperty("sentry.dsn")

    private fun Project.effectiveBuildType(): String = getAndroidBuildTypeOrNull()
        ?: getIOSBuildTypeOrNull()
        ?: getSystemBuildTypeOrRelease()

    private fun Project.getAndroidBuildTypeOrNull(): String? {
        val taskRequests = gradle.startParameter.taskRequests.toString()
        return when {
            taskRequests.contains("Debug", ignoreCase = true) -> AppBuildType.DEBUG.key
            taskRequests.contains("Staging", ignoreCase = true) -> AppBuildType.STAGING.key
            taskRequests.contains("Release", ignoreCase = true) -> AppBuildType.RELEASE.key
            else -> null
        }
    }

    private fun getIOSBuildTypeOrNull(): String? =
        System.getenv("CONFIGURATION")?.lowercase()

    private fun getSystemBuildTypeOrRelease(): String =
        System.getenv().getOrDefault("EFFECTIVE_BUILD_TYPE", AppBuildType.RELEASE.key)

    enum class AppBuildType(val key: String) {
        DEBUG("debug"),
        STAGING("staging"),
        RELEASE("release");

        companion object {
            fun byKey(key: String): AppBuildType = requireNotNull(values().find { it.key == key }) {
                "BuildType with key $key does not exist"
            }
        }
    }
}
//
//sentry {
//    autoUploadProguardMapping = true
//}
//android {
//
//    sentry {
//        // Disables or enables debug log output, e.g. for for sentry-cli.
//        // Default is disabled.
//        debug = true
//        // The slug of the Sentry organization to use for uploading proguard mappings/source contexts.
//        org = "mozaik-ws"
//        // The slug of the Sentry project to use for uploading proguard mappings/source contexts.
//        projectName = "kmptemplate-android"
//        // The authentication token to use for uploading proguard mappings/source contexts.
//        // WARNING: Do not expose this token in your build.gradle files, but rather set an environment
//        // variable and read it into this property.
//        authToken = System.getenv("SENTRY_AUTH_TOKEN")
//        // Disables or enables the handling of Proguard mapping for Sentry.
//        // If enabled the plugin will generate a UUID and will take care of
//        // uploading the mapping to Sentry. If disabled, all the logic
//        // related to proguard mapping will be excluded.
//        // Default is enabled.
//        includeProguardMapping = true
//        // Whether the plugin should attempt to auto-upload the mapping file to Sentry or not.
//        // If disabled the plugin will run a dry-run and just generate a UUID.
//        // The mapping file has to be uploaded manually via sentry-cli in this case.
//        // Default is enabled.
//        autoUploadProguardMapping = true
//        // Disables or enables the automatic configuration of Native Symbols
//        // for Sentry. This executes sentry-cli automatically so
//        // you don't need to do it manually.
//        // Default is disabled.
//        uploadNativeSymbols = false
//        // Whether the plugin should attempt to auto-upload the native debug symbols to Sentry or not.
//        // If disabled the plugin will run a dry-run.
//        // Default is enabled.
//        autoUploadNativeSymbols = true
//        // Does or doesn't include the source code of native code for Sentry.
//        // This executes sentry-cli with the --include-sources param. automatically so
//        // you don't need to do it manually.
//        // This option has an effect only when [uploadNativeSymbols] is enabled.
//        // Default is disabled.
//        includeNativeSources = false
//        // Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
//        // This enables source context, allowing you to see your source
//        // code as part of your stack traces in Sentry.
//        includeSourceContext = false
//        // Configure additional directories to be included in the source bundle which is used for
//        // source context. The directories should be specified relative to the Gradle module/project's
//        // root. For example, if you have a custom source set alongside 'main', the parameter would be
//        // 'src/custom/java'.
//
//        // Enable auto-installation of Sentry components (sentry-android SDK and okhttp, timber and fragment integrations).
//        // Default is enabled.
//        // Only available v3.1.0 and above.
//        autoInstallation {
//            enabled = true
//            // Specifies a version of the sentry-android SDK and fragment, timber and okhttp integrations.
//            //
//            // This is also useful, when you have the sentry-android SDK already included into a transitive dependency/module and want to
//            // align integration versions with it (if it's a direct dependency, the version will be inferred).
//            //
//            // NOTE: if you have a higher version of the sentry-android SDK or integrations on the classpath, this setting will have no effect
//            // as Gradle will resolve it to the latest version.
//            //
//            // Defaults to the latest published Sentry version.
//        }
//        // Disables or enables dependencies metadata reporting for Sentry.
//        // If enabled, the plugin will collect external dependencies and
//        // upload them to Sentry as part of events. If disabled, all the logic
//        // related to the dependencies metadata report will be excluded.
//        //
//        // Default is enabled.
//        includeDependenciesReport = true
//        // Whether the plugin should send telemetry data to Sentry.
//        // If disabled the plugin won't send telemetry data.
//        // This is auto disabled if running against a self hosted instance of Sentry.
//        // Default is enabled.
//        telemetry = true
//    }
//
//}
