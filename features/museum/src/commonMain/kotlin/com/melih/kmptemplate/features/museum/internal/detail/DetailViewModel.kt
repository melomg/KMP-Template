package com.melih.kmptemplate.features.museum.internal.detail

import androidx.lifecycle.ViewModel
import com.melih.kmptemplate.core.shared.domain.api.MuseumRepository
import com.melih.kmptemplate.core.shared.model.MuseumObject
import kotlinx.coroutines.flow.Flow

internal class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
