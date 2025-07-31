package com.melih.kmptemplate.shared.model.platform

interface Platform {
    val appVersionCode: Int
    val appVersionName: String
    val platformVersionName: String
    val buildType: BuildType
    val isDebuggable: Boolean
}

enum class BuildType {
    DEBUG,
    STAGING,
    RELEASE,
}
