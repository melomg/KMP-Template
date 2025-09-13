package com.melih.kmptemplate.shared.logging.internal

internal fun Any.resolveTagName(): String? =
    this::class.simpleName?.substringBefore("$$")?.substringAfter('$')
