package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme
import com.melih.kmptemplate.features.museum.api.MuseumListDestination
import com.melih.kmptemplate.features.museum.api.museumDestinations

@Composable
@Preview
fun App() {
    AppEnvironment {
        AppTheme {
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = MuseumListDestination) {
                museumDestinations(navController)
            }
        }
    }
}
