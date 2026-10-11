package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.internal.DefaultMoviesRepository
import com.melih.kmptemplate.core.shared.data.internal.local.InMemoryMoviesDataSource
import com.melih.kmptemplate.core.shared.data.internal.remote.RemoteMoviesDataSource
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.network.api.di.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Qualifier {
    val APPLICATION_SCOPE = named("applicationScope")
}

val dataModule = module {
    includes(networkModule)

    single<MoviesRepository> {
        DefaultMoviesRepository(get(Qualifier.APPLICATION_SCOPE), get(), get())
    }

    factory { RemoteMoviesDataSource(get()) }
    factory { InMemoryMoviesDataSource() }
}
