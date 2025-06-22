package com.melih.kmptemplate.shared.network

import com.melih.kmptemplate.shared.model.MuseumObject

interface MuseumApi {

    suspend fun getData(): List<MuseumObject>
}
