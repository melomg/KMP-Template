package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import com.melih.kmptemplate.core.shared.designsystem.api.environment.LocalAppDensity
import com.melih.kmptemplate.core.shared.designsystem.api.environment.LocalAppLocaleIso
import com.melih.kmptemplate.core.shared.designsystem.api.environment.LocalAppTheme
import com.melih.kmptemplate.core.shared.designsystem.api.environment.customAppDensity
import com.melih.kmptemplate.core.shared.designsystem.api.environment.customAppLocaleIso
import com.melih.kmptemplate.core.shared.designsystem.api.environment.customAppThemeIsDark

/**
 * Taken from https://youtrack.jetbrains.com/issue/CMP-4197
 * Provides a way to programmatically change the local `ResourceEnvironment`.
 */
@Composable
fun AppEnvironment(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppDensity provides customAppDensity,
        LocalAppTheme provides customAppThemeIsDark,
        LocalAppLocaleIso provides customAppLocaleIso,
    ) {
        key(customAppDensity, customAppThemeIsDark, customAppLocaleIso) {
            content()
        }
    }
}
