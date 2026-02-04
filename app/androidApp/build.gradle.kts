import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

val appProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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
            fun byKey(key: String): AppBuildType = requireNotNull(entries.find { it.key == key }) {
                "BuildType with key $key does not exist"
            }
        }
    }
}
