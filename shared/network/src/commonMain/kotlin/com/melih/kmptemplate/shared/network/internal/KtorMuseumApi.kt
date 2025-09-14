package com.melih.kmptemplate.shared.network.internal

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.melih.kmptemplate.shared.model.MuseumObject
import com.melih.kmptemplate.shared.network.MuseumApi
import io.ktor.utils.io.core.Closeable
import kotlinx.io.IOException

internal class KtorMuseumApi(private val client: HttpClient) : MuseumApi, Closeable {

    override suspend fun getData(): List<MuseumObject> {
        throw IOException("Parcelized error")
        return emptyList()
    }

//    override suspend fun getData(): List<MuseumObject> = safeApiCall {
//        client.get(API_BASE_URL).body<List<MuseumObject>>()
//    } ?: emptyList()

    override fun close() {
        client.close()
    }

    private companion object {
        private const val API_BASE_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }
}
