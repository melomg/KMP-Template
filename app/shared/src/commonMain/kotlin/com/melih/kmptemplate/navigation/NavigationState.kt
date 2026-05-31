package com.melih.kmptemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.SavedStateConfiguration.Companion.DEFAULT
import kotlinx.serialization.PolymorphicSerializer

/**
 * Create a navigation state that persists config changes and process death.
 */
@Composable
internal fun rememberNavigationState(
    startDestination: NavKey,
    topLevelDestinations: Set<NavKey>,
    configuration: SavedStateConfiguration = DEFAULT,
): NavigationState {
    val topLevelDestination = rememberSerializable(
        startDestination, topLevelDestinations,
        configuration = configuration,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startDestination)
    }

    val backStacks = topLevelDestinations.associateWith { key ->
        rememberNavBackStack(configuration, key)
    }

    return remember(startDestination, topLevelDestinations) {
        NavigationState(
            startDestination = startDestination,
            topLevelDestination = topLevelDestination,
            backStacks = backStacks,
        )
    }
}

/**
 * State holder for navigation state.
 *
 * @param startDestination - the start destination. The user will exit the app through this destination.
 * @param topLevelDestination - the current top level destination
 * @param backStacks - the back stacks for each top level destination
 */
internal class NavigationState(
    val startDestination: NavKey,
    topLevelDestination: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelDestination: NavKey by topLevelDestination
    val stacksInUse: List<NavKey>
        get() = if (topLevelDestination == startDestination) {
            listOf(startDestination)
        } else {
            listOf(startDestination, topLevelDestination)
        }
}

/**
 * Convert NavigationState into NavEntries.
 */
@Composable
internal fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
