package com.melih.kmptemplate.features.museum.api

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.melih.kmptemplate.core.shared.navigation.api.ListDetailScene
import com.melih.kmptemplate.core.shared.navigation.api.Navigator
import com.melih.kmptemplate.features.museum.internal.detail.DetailScreen
import com.melih.kmptemplate.features.museum.internal.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface MuseumDestination : NavKey

@Serializable
object MuseumListDestination : MuseumDestination

@Serializable
data class MuseumDetailDestination(val objectId: Int) : MuseumDestination

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.museumDestinations(navigator: Navigator) {
    entry<MuseumListDestination>(
        metadata = ListDetailScene.listPane(),
    ) {
        ListScreen(
            onMuseumDetailClicked = { objectId ->
                navigator.navigate(MuseumDetailDestination(objectId))
            },
        )
    }
    entry<MuseumDetailDestination>(
        metadata = ListDetailScene.detailPane(),
    ) {
        DetailScreen(
            objectId = it.objectId,
            onBackClicked = { navigator.goBack() },
        )
    }
}
