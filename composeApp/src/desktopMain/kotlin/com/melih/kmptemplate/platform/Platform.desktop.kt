@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

private val osName: String
    get() = System.getProperty("os.name")
private val osVersion: String
    get() = System.getProperty("os.version")
private val javaVersion: String
    get() = System.getProperty("java.version")

actual val platformVersionName = "$osName $osVersion (Java $javaVersion)"
