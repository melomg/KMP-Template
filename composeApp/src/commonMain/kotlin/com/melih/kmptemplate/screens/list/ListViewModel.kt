package com.melih.kmptemplate.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melih.kmptemplate.data.MuseumRepository
import com.melih.kmptemplate.shared.model.MuseumObject
import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    platform: Platform,
    museumRepository: MuseumRepository
) : ViewModel() {
    val objects: StateFlow<Pair<List<MuseumObject>, Platform>> =
        museumRepository.getObjects()
            .map {
                it to platform
            }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(
                    emptyList(),
                    object : Platform {
                        override val appVersionCode: Int
                            get() = -1
                        override val appVersionName: String
                            get() = "UNKNOWN"
                        override val platformVersionName: String
                            get() = "UNKNOWN"
                        override val buildType: BuildType
                            get() = BuildType.RELEASE
                        override val isDebuggable: Boolean
                            get() = false

                    }
                )
            )
}
