package com.melih.kmptemplate.shared.logging
//
//internal fun Any.resolveTagName(): String {
//    val simpleName = this::class.simpleName
//    val fullClassName = simpleName ?: "KMPTemplate"
//    val outerClassName = fullClassName.substringBefore('$')
//    val simplerOuterClassName = outerClassName.substringAfterLast('.')
//    return if (simplerOuterClassName.isEmpty()) {
//        fullClassName
//    } else {
//        simplerOuterClassName.removeSuffix("Kt")
//    }
//}

internal fun Any.resolveTagName(): String? =
    this::class.simpleName?.substringBefore('$')
