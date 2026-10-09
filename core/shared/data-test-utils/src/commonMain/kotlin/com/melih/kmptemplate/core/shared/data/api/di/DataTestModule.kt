package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataTestModule = module {
    single { TestMoviesRepository() } bind MoviesRepository::class
}
