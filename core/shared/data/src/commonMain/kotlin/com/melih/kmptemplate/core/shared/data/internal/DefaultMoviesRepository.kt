package com.melih.kmptemplate.core.shared.data.internal

import com.melih.kmptemplate.core.shared.data.internal.local.InMemoryMoviesDataSource
import com.melih.kmptemplate.core.shared.data.internal.remote.RemoteMoviesDataSource
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.threading.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DefaultMoviesRepository(
    applicationScope: CoroutineScope,
    private val remote: RemoteMoviesDataSource,
    private val cache: InMemoryMoviesDataSource,
) : MoviesRepository {

    init {
        applicationScope.launch(DispatcherProvider.IO) {
            refresh()
        }
    }

    override suspend fun refresh() {
        // TODO: Implement pagination instead of fetching first page only
        remote.getPopularMovies(page = 1)?.let { movies ->
            cache.saveMovies(movies)
        }
    }

    override fun getMovies() = cache.getMovies()

    override fun getMovieById(movieId: Long) = cache.getMovieById(movieId)
}
