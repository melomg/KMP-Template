package com.melih.kmptemplate.core.shared.designsystem.api.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var customAppLocaleIso by mutableStateOf<String?>(null)

expect object LocalAppLocaleIso {

    val current: String @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}
