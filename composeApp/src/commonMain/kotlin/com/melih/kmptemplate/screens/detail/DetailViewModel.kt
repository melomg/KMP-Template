package com.melih.kmptemplate.screens.detail

import androidx.lifecycle.ViewModel
import com.melih.kmptemplate.data.MuseumRepository
import com.melih.kmptemplate.shared.model.MuseumObject
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
