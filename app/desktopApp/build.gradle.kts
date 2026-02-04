import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

val appProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    dependencies {
        implementation(projects.app.shared)
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
        implementation(libs.ktor.client.cio)
        implementation(libs.logback)
    }
}

compose.desktop {
    application {
        mainClass = "com.melih.kmptemplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appProperties.appIdBase
            packageVersion = appProperties.versionNameBase

            // TODO Template: Change app icons
            macOS {
                iconFile.set(project.file("src/main/composeResources/app-icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/composeResources/app-icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/composeResources/app-icon.png"))
            }
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
