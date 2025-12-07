package com.melih.kmptemplate.core.shared.network

import com.melih.kmptemplate.core.shared.model.MuseumObject

interface MuseumApi {

    suspend fun getData(): List<MuseumObject>
}
