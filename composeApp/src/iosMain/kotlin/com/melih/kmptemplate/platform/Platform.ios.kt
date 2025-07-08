@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform as NativePlatform

internal class IOSPlatform : Platform {

    private val bundle: NSBundle
        get() = NSBundle.mainBundle
    private val systemName: String
        get() = UIDevice.currentDevice.systemName
    private val systemVersion: String
        get() = UIDevice.currentDevice.systemVersion

    override val appVersionCode: Int
        get() = bundle.objectForInfoDictionaryKey("CFBundleVersion") as? Int ?: 1

    override val appVersionName: String
        get() = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
            ?: "1.0.0"

    override val platformVersionName: String
        get() = "$systemName $systemVersion"

    // TODO: How to get the staging build type?
    @OptIn(ExperimentalNativeApi::class)
    override val buildType: BuildType
        get() = if (NativePlatform.isDebugBinary) BuildType.DEBUG else BuildType.RELEASE

    @OptIn(ExperimentalNativeApi::class)
    override val isDebuggable: Boolean
        get() = NativePlatform.isDebugBinary
}

actual fun platformModule() = module {
    single<Platform> { IOSPlatform() }
}
