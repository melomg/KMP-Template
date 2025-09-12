package com.melih.kmptemplate.shared.logging

internal fun Any.resolveTagName(): String? =
    this::class.simpleName?.substringBefore("$$")?.substringAfter('$')
