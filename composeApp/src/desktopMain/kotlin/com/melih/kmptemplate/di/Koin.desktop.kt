package com.melih.kmptemplate.di

import com.melih.kmptemplate.shared.model.platform.BuildInfo
import com.melih.kmptemplate.shared.model.platform.BuildType
import java.lang.management.ManagementFactory

actual fun createBuildInfo(): BuildInfo {
    val osName = System.getProperty("os.name")
    val osVersion = System.getProperty("os.version")
    val javaVersion = System.getProperty("java.version")

    return BuildInfo(
        appVersionCode = 1, // You might want to read this from a properties file or build configuration
        appVersionName = "1.0.0", // Same here
        platformVersionName = "$osName $osVersion (Java $javaVersion)",
        buildType = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE,
        isDebuggable = false,
    )
//
//    val buildType = System.getProperty("app.buildType", "release")
//    val flavor = System.getProperty("app.flavor", "")
//
//    return BuildInfo(
//        appVersionCode = System.getProperty("app.versionCode", "1").toIntOrNull() ?: 1,
//        appVersionName = System.getProperty("app.versionName", "1.0.0"),
//        platformVersionName = "Desktop: ${System.getProperty("os.name")}",
//        buildType = BuildType.DEBUG,
//    )
}

private fun isDebugBuild(): Boolean {
    // Check if we're running in debug mode
    // This checks for common debug system properties
    return System.getProperty("java.class.path")?.contains("debug") == true ||
            System.getProperty("sun.java.command")?.contains("debug") == true ||
            ManagementFactory.getRuntimeMXBean().inputArguments
                .any { it.contains("debug") || it.contains("jdwp") }
}
