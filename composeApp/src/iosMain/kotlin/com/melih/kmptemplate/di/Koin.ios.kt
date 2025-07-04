package com.melih.kmptemplate.di

import com.melih.kmptemplate.shared.model.platform.BuildInfo
import com.melih.kmptemplate.shared.model.platform.BuildType
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun createBuildInfo(): BuildInfo {
    val bundle = NSBundle.mainBundle
    val appVersionCode = bundle.objectForInfoDictionaryKey("CFBundleVersion") as? Int ?: 1
    val appVersionName =
        bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"
    val systemName = UIDevice.currentDevice.systemName
    val systemVersion = UIDevice.currentDevice.systemVersion

    return BuildInfo(
        appVersionCode = appVersionCode,
        appVersionName = appVersionName,
        platformVersionName = "$systemName $systemVersion",
        buildType = if (Platform.isDebugBinary) BuildType.DEBUG else BuildType.RELEASE,
        isDebuggable = false,
    )
}
