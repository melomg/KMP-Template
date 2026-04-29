@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.core.shared.designsystem.api.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocaleIso {

    private var default: Locale? = null

    @Suppress("MemberNameEqualsClassName")
    private val LocalAppLocaleIso = staticCompositionLocalOf { Locale.getDefault().toString() }

    @Suppress("unused")
    actual val current: String
        @Composable get() = LocalAppLocaleIso.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        if (default == null) {
            default = Locale.getDefault()
        }

        val new = when (value) {
            null -> default!!
            else -> Locale(value)
        }
        Locale.setDefault(new)

        return LocalAppLocaleIso.provides(new.toString())
    }
}
