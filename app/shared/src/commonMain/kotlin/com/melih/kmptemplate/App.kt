package com.melih.kmptemplate

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.melih.kmptemplate.core.shared.designsystem.api.theme.AppTheme

@Composable
fun App() {
    InitCoil()

    AppEnvironment {
        AppTheme {
            AppNavDisplay()
        }
    }
}

@Composable
private fun InitCoil() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}
