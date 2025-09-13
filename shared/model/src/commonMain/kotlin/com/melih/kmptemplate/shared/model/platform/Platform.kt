package com.melih.kmptemplate.shared.model.platform

data class Platform(
    val appName: String,
    val appVersionCode: Int,
    val appVersionName: String,
    val platformVersionName: String,
    val effectiveBuildType: BuildType,
    val isDebuggable: Boolean,
    val sentryDSN: String,
)

enum class BuildType(val key: String) {
    DEBUG("debug"),
    STAGING("staging"),
    RELEASE("release");

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun byKey(key: String): BuildType = requireNotNull(
            entries.find { it.key == key }
        ) { "BuildType with key $key does not exist" }
    }
}
