package com.melih.kmptemplate.features.movies.api

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.melih.kmptemplate.core.shared.navigation.api.ListDetailScene
import com.melih.kmptemplate.core.shared.navigation.api.Navigator
import com.melih.kmptemplate.features.movies.internal.detail.MovieDetailScreen
import com.melih.kmptemplate.features.movies.internal.list.MovieListScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface MoviesDestination : NavKey

@Serializable
object MovieListDestination : MoviesDestination

@Serializable
data class MovieDetailDestination(val movieId: Long) : MoviesDestination

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.moviesDestinations(navigator: Navigator) {
    entry<MovieListDestination>(
        metadata = ListDetailScene.listPane(),
    ) {
        MovieListScreen(
            onMovieDetailClicked = { movieId ->
                navigator.navigate(MovieDetailDestination(movieId))
            },
        )
    }
    entry<MovieDetailDestination>(
        metadata = ListDetailScene.detailPane(),
    ) {
        MovieDetailScreen(
            movieId = it.movieId,
            onBackClicked = { navigator.goBack() },
        )
    }
}
