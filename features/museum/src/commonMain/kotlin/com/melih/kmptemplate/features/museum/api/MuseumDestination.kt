package com.melih.kmptemplate.features.museum.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.melih.kmptemplate.features.museum.internal.detail.DetailScreen
import com.melih.kmptemplate.features.museum.internal.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
object MuseumListDestination

@Serializable
data class MuseumDetailDestination(val objectId: Int)

fun NavGraphBuilder.museumDestinations(navController: NavHostController) {
    composable<MuseumListDestination> {
        ListScreen(navigateToDetails = { objectId ->
            navController.navigate(MuseumDetailDestination(objectId))
        })
    }
    composable<MuseumDetailDestination> { backStackEntry ->
        DetailScreen(
            objectId = backStackEntry.toRoute<MuseumDetailDestination>().objectId,
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}
