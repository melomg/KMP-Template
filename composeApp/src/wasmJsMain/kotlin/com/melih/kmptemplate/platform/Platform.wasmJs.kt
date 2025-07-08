@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

class WasmJsPlatform : Platform {

    private val platform: String
        get() = kotlinx.browser.window.navigator.platform
    private val userAgent: String
        get() = kotlinx.browser.window.navigator.userAgent
    private val vendor: String
        get() = kotlinx.browser.window.navigator.vendor

    override val appVersionCode: Int
        get() = 1 // TODO: You might want to read this from a JS variable or build configuration

    override val appVersionName: String
        get() = "1.0.0" // TODO: You might want to read this from a JS variable or build configuration

    override val platformVersionName: String
        get() = "Web (platform=$platform - userAgent=$userAgent - vendor=$vendor)"

    @OptIn(ExperimentalNativeApi::class)
    override val buildType: BuildType
        get() = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE // TODO: How to get the staging build type?

    override val isDebuggable: Boolean
        get() = isDebugBuild()

    private fun isDebugBuild(): Boolean {
        // a simple check - you might want to use a more sophisticated method
        return kotlinx.browser.window.location.hostname == "localhost" ||
                kotlinx.browser.window.location.hostname == "127.0.0.1" ||
                kotlinx.browser.window.location.search.contains("debug=true")
    }
}

actual fun platformModule() = module {
    single<Platform> { WasmJsPlatform() }
}
