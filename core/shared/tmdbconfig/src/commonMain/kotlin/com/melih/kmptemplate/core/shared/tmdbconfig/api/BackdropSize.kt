package com.melih.kmptemplate.core.shared.tmdbconfig.api

import com.melih.kmptemplate.core.shared.tmdbconfig.internal.SLASH
import com.melih.kmptemplate.core.shared.tmdbconfig.internal.TMDB_BASE_IMAGE_URL

@Suppress("unused")
enum class BackdropSize(val value: String) {
    W300("w300"),
    W780("w780"),
}

fun String.addBaseUrlForSize(size: BackdropSize): String =
    TMDB_BASE_IMAGE_URL + size.value + if (!startsWith(SLASH)) "${SLASH}$this" else this
