import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val appProperties = ApplicationProperties(project)

plugins {
//    alias(libs.plugins.kmptemplate.android.application)
//    alias(libs.plugins.kmptemplate.android.application.compose)
//    alias(libs.plugins.kmptemplate.android.application.flavors)
//    alias(libs.plugins.kmptemplate.android.application.jacoco)
//    alias(libs.plugins.kmptemplate.android.application.firebase)
//    alias(libs.plugins.kmptemplate.hilt)
//    alias(libs.plugins.google.osslicenses)
//    alias(libs.plugins.baselineprofile)
//    alias(libs.plugins.roborazzi)
//    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.melih.kmptemplate.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions.jvmTarget = JvmTarget.JVM_17
        androidResources.enable = true
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    applyDefaultHierarchyTemplate()

    sourceSets {
//        val androidUnitTest by getting
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.cio)
        }
//        androidUnitTest.dependencies {
//            implementation(libs.jetbrains.kotlin.test)
//            implementation(libs.jetbrains.kotlin.test.junit5)
//            runtimeOnly(libs.junit.jupiter.engine)
//        }

        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
            implementation(libs.logback)
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
            api(projects.core.shared.threading)

            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)

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
    }
}

// Android-based preview support
dependencies {
    androidRuntimeClasspath(compose.uiTooling)
    // androidRuntimeClasspath(libs.compose.ui.tooling)
}

// TODO: check why needed
// compose.resources {
//    packageOfResClass = "com.melih.kmptemplate.generated.resources"
// }

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
