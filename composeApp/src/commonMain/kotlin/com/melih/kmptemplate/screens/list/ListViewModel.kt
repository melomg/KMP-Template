package com.melih.kmptemplate.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melih.kmptemplate.data.MuseumRepository
import com.melih.kmptemplate.shared.logging.Klog
import com.melih.kmptemplate.shared.model.MuseumObject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListViewModel(museumRepository: MuseumRepository) : ViewModel() {
    val objects: StateFlow<List<MuseumObject>> =
        museumRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        Klog.verbose { "Verbose log sample" }
        Klog.debug { "Debug log sample" }
        Klog.info { "Info log sample" }
        Klog.warn { "Warn log sample" }
        Klog.error(throwable = IllegalArgumentException("Illegal argument")) { "Error log sample" }
    }
}
