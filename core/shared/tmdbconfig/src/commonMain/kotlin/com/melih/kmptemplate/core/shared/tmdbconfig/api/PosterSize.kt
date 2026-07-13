package com.melih.kmptemplate.core.shared.tmdbconfig.api

import com.melih.kmptemplate.core.shared.tmdbconfig.internal.SLASH
import com.melih.kmptemplate.core.shared.tmdbconfig.internal.TMDB_BASE_IMAGE_URL

@Suppress("unused")
enum class PosterSize(val value: String) {
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
}

fun String.addBaseUrlForSize(size: PosterSize): String =
    TMDB_BASE_IMAGE_URL + size.value + if (!startsWith(SLASH)) "${SLASH}$this" else this
