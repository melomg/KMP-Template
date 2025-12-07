package com.melih.kmptemplate.core.shared.logging.internal

internal fun Any.resolveTagName(): String? =
    this::class.simpleName?.substringBefore("$$")?.substringAfter('$')
