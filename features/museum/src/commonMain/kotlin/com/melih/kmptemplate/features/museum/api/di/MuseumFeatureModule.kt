package com.melih.kmptemplate.features.museum.api.di

import com.melih.kmptemplate.features.museum.internal.detail.DetailViewModel
import com.melih.kmptemplate.features.museum.internal.list.ListViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val museumFeatureModule = module {
    viewModel<ListViewModel>()
    viewModel<DetailViewModel>()
}
