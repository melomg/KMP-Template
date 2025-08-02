@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import platform.UIKit.UIDevice

private val systemName: String
    get() = UIDevice.currentDevice.systemName
private val systemVersion: String
    get() = UIDevice.currentDevice.systemVersion

actual val platformVersionName = "$systemName $systemVersion"
