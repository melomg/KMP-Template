package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

class WasmJsPlatform() : Platform {

    // For this example, using placeholders or hardcoded values.
    // Replace with access to your generated constants, e.g., APP_NAME_WASM
    private val _buildType = "release" // This should be determined at build time
    private val flavor = ""       // This should be determined at build time

    private val platform = kotlinx.browser.window.navigator.platform
    private val userAgent = kotlinx.browser.window.navigator.userAgent
    private val vendor = kotlinx.browser.window.navigator.vendor
    private val cookieEnabled = kotlinx.browser.window.navigator.cookieEnabled
    private val language = kotlinx.browser.window.navigator.language
    private val product = kotlinx.browser.window.navigator.product
    private val productSub = kotlinx.browser.window.navigator.productSub
    private val appCodeName = kotlinx.browser.window.navigator.appCodeName

    override val appVersionCode: Int
        get() = 1 // // You might want to read this from a JS variable or build configuration

    override val appVersionName: String
        get() = kotlinx.browser.window.navigator.appVersion

    override val platformVersionName: String
        get() = "Web (platform=$platform \nuserAgent=$userAgent \nvendor=$vendor \ncookieEnabled=$cookieEnabled \nlanguage=$language \nproduct=$product \nproductSub=$productSub \nappCodeName=$appCodeName)"

    @OptIn(ExperimentalNativeApi::class)
    override val buildType: BuildType
        get() = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE

    override val isDebuggable: Boolean
        get() = false

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
