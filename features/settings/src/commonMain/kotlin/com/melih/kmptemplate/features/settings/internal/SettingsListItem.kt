package com.melih.kmptemplate.features.settings.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.melih.kmptemplate.core.shared.designsystem.api.component.ifNotNull
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme

@Composable
internal fun SettingsListItem(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    modifier = Modifier.weight(1f),
                )
                if (label != null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        maxLines = 2,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
        modifier = modifier
            .ifNotNull(onClick) { clickable(onClick = it) }
    )
}

@Composable
internal fun SettingsListItemDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.padding(start = 16.dp),
    )
}

@Suppress("MaxLineLength")
@Composable
@PreviewScreenSizes
@PreviewLightDark
private fun SettingsListItemPreview() {
    AppTheme {
        Column {
            SettingsListItem(
                title = "Title",
                label = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sed leo augue. Morbi at tristique",
            )
            SettingsListItemDivider()
            SettingsListItem(
                title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sed leo augue. Morbi at tristique",
                label = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sed leo augue. Morbi at tristique",
            )
            SettingsListItemDivider()
            SettingsListItem(
                title = "Second Title",
                label = "Label",
                onClick = {},
            )
        }
    }
}
