package com.melih.kmptemplate.features.settings.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melih.kmptemplate.core.shared.model.platform.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class SettingsViewModel(
    private val platform: Platform,
) : ViewModel() {

    val state: StateFlow<SettingsState> = MutableStateFlow(createState())
        .stateIn(
            initialValue = createState(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
        )

    private fun createState() = SettingsState(
        appVersion = "${platform.appVersionName} (${platform.appVersionCode})",
        platformVersion = platform.platformVersionName,
    )
}
