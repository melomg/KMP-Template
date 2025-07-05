package com.melih.kmptemplate.platform

import com.melih.kmptemplate.shared.model.platform.Platform

expect object PlatformProvider {
    fun getPlatform(): Platform
}
