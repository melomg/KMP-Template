package com.melih.kmptemplate.features.settings.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = label?.let {
            {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                )
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

@Composable
@Preview
private fun SettingsListItemPreview() {
    AppTheme {
        Column {
            SettingsListItem(
                title = "Title",
                label = "Label",
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
