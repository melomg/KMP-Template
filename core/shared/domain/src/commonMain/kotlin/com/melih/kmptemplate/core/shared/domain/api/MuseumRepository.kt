package com.melih.kmptemplate.core.shared.domain.api

import com.melih.kmptemplate.core.shared.model.MuseumObject
import kotlinx.coroutines.flow.Flow

interface MuseumRepository {

    suspend fun refresh()

    fun getObjects(): Flow<List<MuseumObject>>

    fun getObjectById(objectId: Int): Flow<MuseumObject?>
}
