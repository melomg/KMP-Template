package com.melih.kmptemplate.core.shared.data.internal

import com.melih.kmptemplate.core.shared.domain.api.MuseumRepository
import com.melih.kmptemplate.core.shared.model.MuseumObject
import com.melih.kmptemplate.core.shared.network.api.MuseumApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class DefaultMuseumRepository(
    private val museumApi: MuseumApi,
    private val museumStorage: MuseumStorage,
) : MuseumRepository {

    private val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch {
            refresh()
        }
    }

    override suspend fun refresh() {
        museumStorage.saveObjects(museumApi.getData())
    }

    override fun getObjects(): Flow<List<MuseumObject>> = museumStorage.getObjects()

    override fun getObjectById(objectId: Int): Flow<MuseumObject?> =
        museumStorage.getObjectById(objectId)
}
