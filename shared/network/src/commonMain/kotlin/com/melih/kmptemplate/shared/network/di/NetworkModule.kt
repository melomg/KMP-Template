package com.melih.kmptemplate.shared.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.melih.kmptemplate.shared.network.MoviesApi
import com.melih.kmptemplate.shared.network.MuseumApi
import com.melih.kmptemplate.shared.network.internal.KtorMoviesApi
import com.melih.kmptemplate.shared.network.internal.KtorMuseumApi
import org.koin.dsl.module
import io.ktor.client.plugins.logging.Logger as KtorLogger

@Suppress("ForbiddenComment")
val networkModule = module {
    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MoviesApi> { KtorMoviesApi(get()) }

    single {
        HttpClient {
            val json = Json(DefaultJson) { ignoreUnknownKeys = true }

            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }

            expectSuccess = true

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }

            install(Logging) {
                // TODO: Use build types to change the log level
                level = LogLevel.HEADERS
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        // TODO: Enable logger instead
                        println(message)
                        // appLogger.log(LOG_TAG) { message }
                    }
                }
            }
        }
    }
}
