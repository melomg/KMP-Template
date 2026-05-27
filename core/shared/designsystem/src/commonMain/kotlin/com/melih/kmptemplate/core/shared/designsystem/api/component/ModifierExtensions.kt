package com.melih.kmptemplate.core.shared.designsystem.api.component

import androidx.compose.ui.Modifier

inline fun <T : Any> Modifier.ifNotNull(value: T?, builder: Modifier.(T) -> Modifier): Modifier =
    then(if (value != null) builder(value) else this)
