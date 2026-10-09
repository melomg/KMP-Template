package com.melih.kmptemplate.core.shared.data.api

import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.model.Movie
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestMoviesRepository : MoviesRepository {

    private val _movies = MutableSharedFlow<MutableMap<Long, Movie>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    var requestedMovieId: Long? = null
        private set

    override suspend fun refresh() = Unit

    override fun getMovies(): Flow<List<Movie>> = _movies.map { it.values.toList() }

    suspend fun addMovie(movie: Movie) {
        val currentMap = _movies.replayCache.lastOrNull() ?: mutableMapOf()
        currentMap[movie.id] = movie
        _movies.emit(currentMap)
    }

    suspend fun setMovies(movies: List<Movie>) {
        _movies.emit(movies.associateBy { it.id }.toMutableMap())
    }

    override fun getMovieById(movieId: Long): Flow<Movie?> {
        requestedMovieId = movieId
        return _movies.map { it[movieId] }
    }
}
