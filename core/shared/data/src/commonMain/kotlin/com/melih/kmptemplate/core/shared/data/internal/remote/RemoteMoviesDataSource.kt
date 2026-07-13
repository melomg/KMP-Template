package com.melih.kmptemplate.core.shared.data.internal.remote

import com.melih.kmptemplate.core.shared.data.internal.model.MoviesResponse
import com.melih.kmptemplate.core.shared.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.Closeable

internal class RemoteMoviesDataSource(private val client: HttpClient) : Closeable {

    suspend fun getPopularMovies(page: Int): List<Movie>? = safeApiCall {
        client.get("$API_BASE_URL/movie/popular") {
            url { parameters.append("page", page.toString()) }
        }
            .body<MoviesResponse>()
            .mapToDomain()
    }

    override fun close() {
        client.close()
    }

    private companion object {
        private const val API_BASE_URL: String = "https://api.themoviedb.org/3/"
    }
}
