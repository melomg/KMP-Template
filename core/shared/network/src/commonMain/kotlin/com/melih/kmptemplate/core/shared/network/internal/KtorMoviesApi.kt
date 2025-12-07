package com.melih.kmptemplate.core.shared.network.internal

import com.melih.kmptemplate.core.shared.model.Movies
import com.melih.kmptemplate.core.shared.network.MoviesApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.Closeable

internal class KtorMoviesApi(private val client: HttpClient) : MoviesApi, Closeable {

    override suspend fun getPopularMovies(page: Int): Movies? = safeApiCall {
        client.get("$API_BASE_URL/movie/popular").body<Movies>()
    }

    override fun close() {
        client.close()
    }

    private companion object {
        private const val API_BASE_URL: String = "https://api.themoviedb.org/3/"
    }
}
