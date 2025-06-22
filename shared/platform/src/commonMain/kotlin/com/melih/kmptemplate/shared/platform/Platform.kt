package com.melih.kmptemplate.shared.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
