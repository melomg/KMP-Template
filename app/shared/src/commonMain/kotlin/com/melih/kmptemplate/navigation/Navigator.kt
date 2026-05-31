package com.melih.kmptemplate.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
internal class Navigator(val state: NavigationState) {

    fun navigate(destination: NavKey) {
        if (destination in state.backStacks.keys) {
            // This is a top level destination, just switch to it.
            state.topLevelDestination = destination
        } else {
            state.backStacks[state.topLevelDestination]?.add(destination)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelDestination]
            ?: error("Stack for ${state.topLevelDestination} not found")
        val currentDestination = currentStack.last()

        // If we're at the base of the current destination, go back to the start destination stack.
        if (currentDestination == state.topLevelDestination) {
            state.topLevelDestination = state.startDestination
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
