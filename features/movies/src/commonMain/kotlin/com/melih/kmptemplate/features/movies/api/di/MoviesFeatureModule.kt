package com.melih.kmptemplate.features.movies.api.di

import com.melih.kmptemplate.features.movies.internal.detail.MovieDetailViewModel
import com.melih.kmptemplate.features.movies.internal.list.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val moviesFeatureModule = module {
    viewModel { MovieListViewModel(get()) }
    viewModel { params -> MovieDetailViewModel(params.get(), get()) }
}
