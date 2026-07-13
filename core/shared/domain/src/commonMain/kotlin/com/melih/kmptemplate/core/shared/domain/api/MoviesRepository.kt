package com.melih.kmptemplate.core.shared.domain.api

import com.melih.kmptemplate.core.shared.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun refresh()

    fun getMovies(): Flow<List<Movie>>

    fun getMovieById(movieId: Long): Flow<Movie?>
}
