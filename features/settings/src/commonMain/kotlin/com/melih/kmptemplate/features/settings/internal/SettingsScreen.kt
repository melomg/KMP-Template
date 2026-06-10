package com.melih.kmptemplate.features.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme
import com.melih.kmptemplate.core.shared.l10n.Res
import com.melih.kmptemplate.core.shared.l10n.back
import com.melih.kmptemplate.core.shared.l10n.settings_app_version
import com.melih.kmptemplate.core.shared.l10n.settings_open_source
import com.melih.kmptemplate.core.shared.l10n.settings_platform_version
import com.melih.kmptemplate.core.shared.l10n.settings_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    onBackClicked: () -> Unit,
    onOpenSourceClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel<SettingsViewModel>(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SettingsTopAppBar(
                onNavigationBackClicked = onBackClicked,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize()
        ) {
            SettingsListItem(
                title = stringResource(Res.string.settings_app_version),
                label = state.appVersion,
            )

            SettingsListItemDivider()

            SettingsListItem(
                title = stringResource(Res.string.settings_platform_version),
                label = state.platformVersion,
            )

            SettingsListItemDivider()

            SettingsListItem(
                title = stringResource(Res.string.settings_open_source),
                onClick = onOpenSourceClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(
    onNavigationBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun TopAppBarPreviews() {
    AppTheme {
        Scaffold { padding ->
            SettingsTopAppBar(
                onNavigationBackClicked = {},
                modifier = Modifier.padding(padding),
            )
        }
    }
}
