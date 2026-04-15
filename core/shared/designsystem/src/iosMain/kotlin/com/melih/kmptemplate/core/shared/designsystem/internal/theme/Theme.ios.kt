package com.melih.kmptemplate.core.shared.designsystem.internal.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
internal actual fun SystemAppearance(
    surfaceColor: Color,
    darkTheme: Boolean
) {
    LaunchedEffect(darkTheme) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (darkTheme) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
        )
    }
}
