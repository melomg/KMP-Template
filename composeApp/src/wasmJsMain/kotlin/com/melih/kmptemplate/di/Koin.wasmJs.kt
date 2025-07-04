package com.melih.kmptemplate.di

import com.melih.kmptemplate.shared.model.platform.BuildInfo
import com.melih.kmptemplate.shared.model.platform.BuildType

actual fun createBuildInfo(): BuildInfo {
    // For this example, using placeholders or hardcoded values.
    // Replace with access to your generated constants, e.g., APP_NAME_WASM
    val buildType = "release" // This should be determined at build time
    val flavor = ""       // This should be determined at build time

    val platform = kotlinx.browser.window.navigator.platform
    val userAgent = kotlinx.browser.window.navigator.userAgent
    val vendor = kotlinx.browser.window.navigator.vendor
    val cookieEnabled = kotlinx.browser.window.navigator.cookieEnabled
    val language = kotlinx.browser.window.navigator.language
    val product = kotlinx.browser.window.navigator.product
    val productSub = kotlinx.browser.window.navigator.productSub
    val appCodeName = kotlinx.browser.window.navigator.appCodeName
    val appVersionName = kotlinx.browser.window.navigator.appVersion

    return BuildInfo(
        appVersionCode =  1, // You might want to read this from a JS variable or build configuration
        appVersionName = appVersionName, // Same here
        platformVersionName = "Web (platform=$platform \nuserAgent=$userAgent \nvendor=$vendor \ncookieEnabled=$cookieEnabled \nlanguage=$language \nproduct=$product \nproductSub=$productSub \nappCodeName=$appCodeName)",
        buildType = if (isDebugBuild()) BuildType.DEBUG else BuildType.RELEASE,
        isDebuggable = false,
    )
}

private fun isDebugBuild(): Boolean {
    // a simple check - you might want to use a more sophisticated method
    return kotlinx.browser.window.location.hostname == "localhost" ||
            kotlinx.browser.window.location.hostname == "127.0.0.1" ||
            kotlinx.browser.window.location.search.contains("debug=true")
}
