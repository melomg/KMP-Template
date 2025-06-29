@file:Suppress("MatchingDeclarationName")
package com.melih.kmptemplate.shared.platform

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
