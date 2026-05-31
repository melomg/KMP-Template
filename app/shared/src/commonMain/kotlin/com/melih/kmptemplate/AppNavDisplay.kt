package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.melih.kmptemplate.features.museum.api.MuseumDetailDestination
import com.melih.kmptemplate.features.museum.api.MuseumListDestination
import com.melih.kmptemplate.features.museum.api.museumDestinations
import com.melih.kmptemplate.features.settings.api.SettingsDestination
import com.melih.kmptemplate.features.settings.api.settingsDestinations
import com.melih.kmptemplate.navigation.Navigator
import com.melih.kmptemplate.navigation.rememberNavigationState
import com.melih.kmptemplate.navigation.toEntries
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val configuration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MuseumListDestination::class, MuseumListDestination.serializer())
            subclass(MuseumDetailDestination::class, MuseumDetailDestination.serializer())
            subclass(SettingsDestination::class, SettingsDestination.serializer())
        }
    }
}

private val TOP_LEVEL_DESTINATIONS = setOf<NavKey>(
    MuseumListDestination,
)

@Composable
internal fun AppNavDisplay(
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberNavigationState(
        startDestination = MuseumListDestination,
        topLevelDestinations = TOP_LEVEL_DESTINATIONS,
        configuration = configuration,
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        museumDestinations(
            onMuseumDetailClicked = { objectId ->
                navigator.navigate(MuseumDetailDestination(objectId))
            },
            onBackClicked = { navigator.goBack() },
        )
        settingsDestinations(
            onOpenSourceClicked = { TODO("Not implemented yet!") },
            onBackClicked = { navigator.goBack() },
        )
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        modifier = modifier,
    )
}
