@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import com.melih.kmptemplate.BuildConfig
import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class AndroidPlatform(
//    private val appContext: Context
) : Platform {

    init {
        println("")
    }

    override val appVersionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val appVersionName: String
        get() = BuildConfig.VERSION_NAME

    override val platformVersionName: String
        get() = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    override val buildType: BuildType
        get() = when (BuildConfig.BUILD_TYPE) {
            "debug" -> BuildType.DEBUG
            "staging" -> BuildType.STAGING
            else -> BuildType.RELEASE
        }

    override val isDebuggable: Boolean
        get() = false//(appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) == 0
}

actual object PlatformProvider {
    actual fun getPlatform(): Platform = AndroidPlatform()
}
