package com.melih.kmptemplate.features.museum.api

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

fun EntryProviderScope<NavKey>.museumDestinations(
    onMuseumDetailClicked: (objectId: Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    entry<MuseumListDestination> {
        ListScreen(onMuseumDetailClicked = onMuseumDetailClicked)
    }
    entry<MuseumDetailDestination> {
        DetailScreen(
            objectId = it.objectId,
            navigateBack = onBackClicked,
        )
    }
}
