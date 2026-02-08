@file:Suppress("unused")

package com.melih.kmptemplate

import org.gradle.api.Project
import java.io.File
import java.util.Properties

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
