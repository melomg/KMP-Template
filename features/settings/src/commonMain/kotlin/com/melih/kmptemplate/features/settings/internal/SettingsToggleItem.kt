package com.melih.kmptemplate.features.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme

@Composable
internal fun SettingsToggleItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        trailingContent = {
            Switch(
                checked = isChecked,
                onCheckedChange = null, // Handled by toggleable modifier
            )
        },
        modifier = modifier
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
    )
}

@Composable
@Preview
private fun SettingsToggleListItemPreview() {
    AppTheme {
        Column {
            SettingsToggleItem(
                title = "Categories",
                subtitle = "Yes",
                isChecked = true,
                onCheckedChange = {},
            )

            SettingsListItemDivider()

            SettingsToggleItem(
                title = "Categories",
                subtitle = "No",
                isChecked = false,
                onCheckedChange = {},
            )
        }
    }
}
