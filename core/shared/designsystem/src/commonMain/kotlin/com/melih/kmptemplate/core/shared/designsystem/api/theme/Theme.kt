package com.melih.kmptemplate.core.shared.designsystem.api.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.melih.kmptemplate.core.shared.designsystem.internal.theme.DarkColorScheme
import com.melih.kmptemplate.core.shared.designsystem.internal.theme.LightColorScheme
import com.melih.kmptemplate.core.shared.designsystem.internal.theme.SystemAppearance
import com.melih.kmptemplate.core.shared.designsystem.internal.theme.Typography

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    SystemAppearance(colorScheme.surface, darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = { Surface(content = content) }
    )
}
