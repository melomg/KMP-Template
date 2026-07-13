package com.melih.kmptemplate.core.shared.model

data class Movie(
    val adult: Boolean,
    val backdropUrl: String,
    val id: Long,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val posterUrl: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: String,
    val voteCount: Int,
)
