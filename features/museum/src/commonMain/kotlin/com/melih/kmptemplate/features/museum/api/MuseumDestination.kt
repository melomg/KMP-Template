package com.melih.kmptemplate.features.museum.api

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
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
fun EntryProviderScope<NavKey>.museumDestinations(
    onMuseumDetailClicked: (objectId: Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    entry<MuseumListDestination>(
        metadata = ListDetailSceneStrategy.listPane()
    ) {
        ListScreen(onMuseumDetailClicked = onMuseumDetailClicked)
    }
    entry<MuseumDetailDestination>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DetailScreen(
            objectId = it.objectId,
            navigateBack = onBackClicked,
        )
    }
}
