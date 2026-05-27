package com.melih.kmptemplate.features.settings.api.di

import com.melih.kmptemplate.features.settings.internal.SettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsFeatureModule = module {
    factoryOf(::SettingsViewModel)
}
