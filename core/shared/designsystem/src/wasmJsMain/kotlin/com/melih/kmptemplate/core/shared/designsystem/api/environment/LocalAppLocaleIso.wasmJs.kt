@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.core.shared.designsystem.api.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Suppress("ClassName")
external object window {
    @Suppress("ObjectPropertyName")
    var __customLocale: String?
}

actual object LocalAppLocaleIso {

    @Suppress("MemberNameEqualsClassName")
    private val LocalAppLocaleIso = staticCompositionLocalOf { Locale.current }

    @Suppress("unused")
    actual val current: String
        @Composable get() = LocalAppLocaleIso.current.toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        window.__customLocale = value?.replace('_', '-')
        return LocalAppLocaleIso.provides(Locale.current)
    }
}
