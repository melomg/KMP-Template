package com.melih.kmptemplate.data

import com.melih.kmptemplate.shared.logging.Klog
import com.melih.kmptemplate.shared.model.MuseumObject
import com.melih.kmptemplate.shared.network.MuseumApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MuseumRepository(
    private val museumApi: MuseumApi,
    private val museumStorage: MuseumStorage,
) {
//    val handler = CoroutineExceptionHandler { _, exception ->
//        Klog.error(tag = "MuseumRepository", throwable = exception) { "" }
//    }

//    private val scope = CoroutineScope(SupervisorJob() + handler)

    suspend fun refresh() {
        museumStorage.saveObjects(museumApi.getData())
    }

    fun getObjects(): Flow<List<MuseumObject>> = museumStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<MuseumObject?> = museumStorage.getObjectById(objectId)
}
