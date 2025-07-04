package com.melih.kmptemplate.di

import android.os.Build
import com.melih.kmptemplate.BuildConfig
import com.melih.kmptemplate.shared.model.platform.BuildInfo
import com.melih.kmptemplate.shared.model.platform.BuildType

actual fun createBuildInfo(): BuildInfo = BuildInfo(
    appVersionCode = BuildConfig.VERSION_CODE,
    appVersionName = BuildConfig.VERSION_NAME,
    platformVersionName = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
    buildType = when (BuildConfig.BUILD_TYPE) {
        "debug" -> BuildType.DEBUG
        "staging" -> BuildType.STAGING
        else -> BuildType.RELEASE
    },
    isDebuggable = false,
)
