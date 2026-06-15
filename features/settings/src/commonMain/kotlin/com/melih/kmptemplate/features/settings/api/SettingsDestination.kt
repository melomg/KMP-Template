package com.melih.kmptemplate.features.settings.api

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.melih.kmptemplate.core.shared.navigation.api.Navigator
import com.melih.kmptemplate.features.settings.internal.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsDestination : NavKey

fun EntryProviderScope<NavKey>.settingsDestinations(navigator: Navigator) {
    entry<SettingsDestination> {
        SettingsScreen(
            onBackClicked = { navigator.goBack() },
            onOpenSourceClicked = { TODO("Not implemented yet!") },
        )
    }
}
