package com.melih.kmptemplate.features.museum.api.di

import com.melih.kmptemplate.features.museum.internal.detail.DetailViewModel
import com.melih.kmptemplate.features.museum.internal.list.ListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val museumFeatureModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}
