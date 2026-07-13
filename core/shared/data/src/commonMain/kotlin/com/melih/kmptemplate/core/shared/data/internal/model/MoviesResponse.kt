package com.melih.kmptemplate.core.shared.data.internal.model

import kotlinx.serialization.Serializable

@Serializable
internal data class MoviesResponse(
    val page: Int,
    val results: List<MovieResponse>,
)
