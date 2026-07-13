package com.melih.kmptemplate.core.shared.data.internal.remote

import com.melih.kmptemplate.core.shared.data.internal.model.MoviesResponse
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.core.shared.tmdbconfig.api.BackdropSize
import com.melih.kmptemplate.core.shared.tmdbconfig.api.PosterSize
import com.melih.kmptemplate.core.shared.tmdbconfig.api.addBaseUrlForSize
import kotlin.math.roundToInt

private const val RATING_PRECISION = 10f

internal fun MoviesResponse.mapToDomain(): List<Movie> = results.map {
    val roundedVoteAverage = (it.voteAverage * RATING_PRECISION).roundToInt() / RATING_PRECISION
    Movie(
        adult = it.adult,
        backdropUrl = it.backdropPath?.addBaseUrlForSize(BackdropSize.W300) ?: "",
        id = it.id,
        originalLanguage = it.originalLanguage,
        originalTitle = it.originalTitle,
        overview = it.overview,
        popularity = it.popularity,
        posterUrl = it.posterPath?.addBaseUrlForSize(PosterSize.W342) ?: "",
        releaseDate = it.releaseDate,
        title = it.title,
        video = it.video,
        voteAverage = roundedVoteAverage.toString(),
        voteCount = it.voteCount,
    )
}
