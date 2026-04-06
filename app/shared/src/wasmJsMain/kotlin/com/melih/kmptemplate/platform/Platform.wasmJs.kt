@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

private val platform: String
    get() = kotlinx.browser.window.navigator.platform
private val userAgent: String
    get() = kotlinx.browser.window.navigator.userAgent
private val vendor: String
    get() = kotlinx.browser.window.navigator.vendor

actual val platformVersionName = "Web (platform=$platform - userAgent=$userAgent - vendor=$vendor)"
