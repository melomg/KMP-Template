package com.melih.kmptemplate.core.shared.model

@Suppress("LongParameterList")
fun fakeMovie(
    id: Long = 1L,
    title: String = "Test Movie",
    overview: String = "Test Overview",
    backdropUrl: String = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
    posterUrl: String = "https://image.tmdb.org/t/p/w500/poster.jpg",
    adult: Boolean = false,
    originalLanguage: String = "en",
    originalTitle: String = "Test Movie",
    popularity: Float = 8.5f,
    releaseDate: String = "2024-01-01",
    video: Boolean = false,
    voteAverage: String = "8.5",
    voteCount: Int = 100,
) = Movie(
    adult = adult,
    backdropUrl = backdropUrl,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)
