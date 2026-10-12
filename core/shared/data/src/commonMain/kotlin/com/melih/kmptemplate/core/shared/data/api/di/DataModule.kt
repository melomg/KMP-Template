package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.internal.DefaultMoviesRepository
import com.melih.kmptemplate.core.shared.data.internal.local.InMemoryMoviesDataSource
import com.melih.kmptemplate.core.shared.data.internal.remote.RemoteMoviesDataSource
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.network.api.di.networkModule
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)

    single<MoviesRepository> {
        DefaultMoviesRepository(get(Qualifiers.APPLICATION_SCOPE), get(), get())
    }

    factory { RemoteMoviesDataSource(get()) }
    factory { InMemoryMoviesDataSource() }
}
