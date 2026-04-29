package com.melih.kmptemplate.core.shared.designsystem.api.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import java.util.Locale

actual object LocalAppLocaleIso {

    private var default: Locale? = null

    @Suppress("unused")
    actual val current: String
        @Composable get() = Locale.getDefault().toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        if (default == null) {
            default = Locale.getDefault()
        }

        val new = when (value) {
            null -> default!!
            else ->  Locale.Builder().setLanguage(value).build()
        }

        Locale.setDefault(new)
        configuration.setLocale(new)

        val resources = LocalResources.current
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return LocalConfiguration.provides(configuration)
    }
}
