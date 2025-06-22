package com.melih.kmptemplate.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    val page: Int,
    val results: List<Movie>,
)
