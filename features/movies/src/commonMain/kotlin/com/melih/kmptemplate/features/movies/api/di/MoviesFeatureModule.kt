package com.melih.kmptemplate.features.movies.api.di

import com.melih.kmptemplate.features.movies.internal.detail.MovieDetailViewModel
import com.melih.kmptemplate.features.movies.internal.list.MovieListViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val moviesFeatureModule = module {
    viewModel<MovieListViewModel>()
    viewModel<MovieDetailViewModel>()
}
