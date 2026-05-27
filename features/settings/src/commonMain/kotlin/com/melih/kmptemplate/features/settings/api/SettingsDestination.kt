package com.melih.kmptemplate.features.settings.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.melih.kmptemplate.features.settings.internal.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsDestination

fun NavGraphBuilder.settingsDestinations(navController: NavHostController) {
    composable<SettingsDestination> {
        SettingsScreen(
            onNavigationBackClicked = { navController.popBackStack() },
            onOpenSourceClicked = {
                // TODO
            },
        )
    }
}
