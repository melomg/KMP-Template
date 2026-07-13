package com.melih.kmptemplate.core.shared.data.internal.local

import com.melih.kmptemplate.core.shared.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class InMemoryMoviesDataSource {

    private val data = MutableStateFlow(emptyList<Movie>())

    fun saveMovies(newObjects: List<Movie>) {
        data.value = newObjects
    }

    fun getMovieById(movieId: Long): Flow<Movie?> = data.map { movies ->
        movies.find { it.id == movieId }
    }

    fun getMovies(): Flow<List<Movie>> = data
}
