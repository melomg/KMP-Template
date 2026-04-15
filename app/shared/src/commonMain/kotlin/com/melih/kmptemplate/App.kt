package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme
import com.melih.kmptemplate.screens.detail.DetailScreen
import com.melih.kmptemplate.screens.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)

@Composable
@Preview
fun App() {
    AppTheme {
        val navController: NavHostController = rememberNavController()
        NavHost(navController = navController, startDestination = ListDestination) {
            composable<ListDestination> {
                ListScreen(navigateToDetails = { objectId ->
                    navController.navigate(DetailDestination(objectId))
                })
            }
            composable<DetailDestination> { backStackEntry ->
                DetailScreen(
                    objectId = backStackEntry.toRoute<DetailDestination>().objectId,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
