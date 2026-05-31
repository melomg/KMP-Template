package com.melih.kmptemplate.features.settings.api

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.melih.kmptemplate.features.settings.internal.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsDestination : NavKey

fun EntryProviderScope<NavKey>.settingsDestinations(
    onOpenSourceClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    entry<SettingsDestination> {
        SettingsScreen(
            onBackClicked = onBackClicked,
            onOpenSourceClicked = onOpenSourceClicked,
        )
    }
}
