@file:Suppress("MatchingDeclarationName")
package com.melih.kmptemplate.shared.platform

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
