package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme

@Composable
fun App() {
    AppEnvironment {
        AppTheme {
            AppNavDisplay()
        }
    }
}
