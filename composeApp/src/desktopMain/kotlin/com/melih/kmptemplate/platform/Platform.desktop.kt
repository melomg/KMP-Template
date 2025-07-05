@file:Suppress("MatchingDeclarationName")
package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.lang.management.ManagementFactory

class DesktopPlatform() : Platform {

    private val osName = System.getProperty("os.name")
    private val osVersion = System.getProperty("os.version")
    private val javaVersion = System.getProperty("java.version")

    override val appVersionCode: Int
        get() = 1 // You might want to read this from a properties file or build configuration

    override val appVersionName: String
        get() = "1.0.0" // Same here

    override val platformVersionName: String
        get() = "$osName $osVersion (Java $javaVersion)"

    override val buildType: BuildType
        get() = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE

    override val isDebuggable: Boolean
        get() = false

    private fun isDebugBuild(): Boolean {
        // Check if we're running in debug mode
        // This checks for common debug system properties
        return System.getProperty("java.class.path")?.contains("debug") == true ||
                System.getProperty("sun.java.command")?.contains("debug") == true ||
                ManagementFactory.getRuntimeMXBean().inputArguments
                    .any { it.contains("debug") || it.contains("jdwp") }
    }
}

actual fun platformModule() = module {
    singleOf(::DesktopPlatform)
}

//
//    val buildType =
//    val flavor = System.getProperty("app.flavor", "")
//
//    return BuildInfo(
//        appVersionCode = System.getProperty("app.versionCode", "1").toIntOrNull() ?: 1,
//        appVersionName = System.getProperty("app.versionName", "1.0.0"),
//        platformVersionName = "Desktop: ${System.getProperty("os.name")}",
//        buildType = BuildType.DEBUG,
//    )
