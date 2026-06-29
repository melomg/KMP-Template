package com.melih.kmptemplate.features.settings.api.di

import com.melih.kmptemplate.features.settings.internal.SettingsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val settingsFeatureModule = module {
    viewModel<SettingsViewModel>()
}
