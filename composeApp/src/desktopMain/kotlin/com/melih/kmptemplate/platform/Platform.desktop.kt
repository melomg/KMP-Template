@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.dsl.module
import java.lang.management.ManagementFactory

internal class DesktopPlatform : Platform {

    private val osName: String
        get() = System.getProperty("os.name")
    private val osVersion: String
        get() = System.getProperty("os.version")
    private val javaVersion: String
        get() = System.getProperty("java.version")

    // TODO: How to get this from properties file or build configuration
    //  Maybe System.getProperty("app.versionCode")?
    override val appVersionCode: Int
        get() = 1

    override val appVersionName: String
        get() = "1.0.0" // TODO: Same here

    override val platformVersionName: String
        get() = "$osName $osVersion (Java $javaVersion)"

    override val buildType: BuildType
        get() = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE // TODO: How to get the staging build type?

    override val isDebuggable: Boolean
        get() = false // TODO: How to get if it's debuggable?

    // TODO: This is NOT correct
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
    single<Platform> { DesktopPlatform() }
}
