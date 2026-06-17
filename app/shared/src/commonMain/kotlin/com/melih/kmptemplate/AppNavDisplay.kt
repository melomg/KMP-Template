package com.melih.kmptemplate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.window.core.layout.WindowSizeClass
import com.melih.kmptemplate.core.shared.l10n.Res
import com.melih.kmptemplate.core.shared.l10n.museums_title
import com.melih.kmptemplate.core.shared.l10n.settings_title
import com.melih.kmptemplate.core.shared.navigation.api.Navigator
import com.melih.kmptemplate.core.shared.navigation.api.rememberListDetailSceneStrategy
import com.melih.kmptemplate.core.shared.navigation.api.rememberNavigationState
import com.melih.kmptemplate.core.shared.navigation.api.toEntries
import com.melih.kmptemplate.features.museum.api.MuseumDetailDestination
import com.melih.kmptemplate.features.museum.api.MuseumListDestination
import com.melih.kmptemplate.features.museum.api.museumDestinations
import com.melih.kmptemplate.features.settings.api.SettingsDestination
import com.melih.kmptemplate.features.settings.api.settingsDestinations
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val configuration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MuseumListDestination::class, MuseumListDestination.serializer())
            subclass(MuseumDetailDestination::class, MuseumDetailDestination.serializer())
            subclass(SettingsDestination::class, SettingsDestination.serializer())
        }
    }
}

private data class NavBarItem(
    val label: StringResource,
    val icon: ImageVector,
    val contentDescription: StringResource,
)

private val TOP_LEVEL_DESTINATIONS: Map<NavKey, NavBarItem> = mapOf(
    MuseumListDestination to NavBarItem(
        label = Res.string.museums_title,
        icon = Icons.Default.Home,
        contentDescription = Res.string.museums_title
    ),
    SettingsDestination to NavBarItem(
        label = Res.string.settings_title,
        icon = Icons.Default.Settings,
        contentDescription = Res.string.settings_title
    ),
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun AppNavDisplay(
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberNavigationState(
        startKey = MuseumListDestination,
        topLevelKeys = TOP_LEVEL_DESTINATIONS.keys,
        configuration = configuration,
    )

    val navigator = remember { Navigator(navigationState) }
    val entryProvider = rememberEntryProvider(navigator)

    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val customNavSuiteType = NavigationSuiteScaffoldDefaults.navigationSuiteType(adaptiveInfo)

    var shouldShowNavBar by remember { mutableStateOf(true) }
    val nestedScrollConnection = rememberNestedScrollConnection(
        windowSizeClass = adaptiveInfo.windowSizeClass,
        shouldShowNavBar = shouldShowNavBar,
        onShowNavBarChange = { shouldShowNavBar = it },
    )

    val isTopLevelScreen by remember {
        derivedStateOf { navigationState.currentKey == navigationState.currentTopLevelKey }
    }
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(
        customNavSuiteType = customNavSuiteType,
        isTopLevelScreen = isTopLevelScreen,
        shouldShowNavBar = shouldShowNavBar,
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_DESTINATIONS.forEach { (destination, navBarItem) ->
                val isSelected = destination == navigationState.currentTopLevelKey
                item(
                    selected = isSelected,
                    onClick = { navigator.navigate(destination) },
                    icon = {
                        Icon(
                            imageVector = navBarItem.icon,
                            contentDescription = stringResource(navBarItem.contentDescription)
                        )
                    },
                    label = { Text(stringResource(navBarItem.label)) },
                )
            }
        },
        layoutType = customNavSuiteType,
        modifier = modifier.nestedScroll(nestedScrollConnection),
        state = navigationSuiteScaffoldState,
    ) {
        val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            sceneStrategies = listOf(listDetailStrategy),
            onBack = { navigator.goBack() },
        )
    }
}

@Composable
private fun rememberNavigationSuiteScaffoldState(
    customNavSuiteType: NavigationSuiteType,
    isTopLevelScreen: Boolean,
    shouldShowNavBar: Boolean,
): NavigationSuiteScaffoldState {
    val navSuiteIsNavigationBar =
        customNavSuiteType == NavigationSuiteType.ShortNavigationBarCompact ||
                customNavSuiteType == NavigationSuiteType.ShortNavigationBarMedium ||
                customNavSuiteType == NavigationSuiteType.NavigationBar

    val state = rememberNavigationSuiteScaffoldState()

    LaunchedEffect(navSuiteIsNavigationBar, isTopLevelScreen, shouldShowNavBar) {
        if (navSuiteIsNavigationBar) {
            when {
                !isTopLevelScreen -> state.hide()
                shouldShowNavBar -> state.show()
                else -> state.hide()
            }
        } else {
            state.show()
        }
    }

    return state
}

@Composable
private fun rememberNestedScrollConnection(
    windowSizeClass: WindowSizeClass,
    shouldShowNavBar: Boolean,
    onShowNavBarChange: (Boolean) -> Unit,
): NestedScrollConnection = remember(windowSizeClass, shouldShowNavBar) {
    object : NestedScrollConnection {
        private val SCROLL_THRESHOLD = 10f

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val isHeightCompact = !windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
            )
            // Only change visibility of nav bar if height is compact
            if (isHeightCompact) {
                if (available.y < -SCROLL_THRESHOLD && shouldShowNavBar) {
                    onShowNavBarChange(false)
                } else if (available.y > SCROLL_THRESHOLD && !shouldShowNavBar) {
                    onShowNavBarChange(true)
                }
            }
            return Offset.Zero
        }
    }
}

@Composable
private fun rememberEntryProvider(navigator: Navigator) = remember(navigator) {
    entryProvider {
        museumDestinations(navigator)
        settingsDestinations(navigator)
    }
}
