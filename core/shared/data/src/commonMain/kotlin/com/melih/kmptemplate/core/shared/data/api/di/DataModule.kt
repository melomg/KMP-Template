package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.internal.DefaultMoviesRepository
import com.melih.kmptemplate.core.shared.data.internal.local.InMemoryMoviesDataSource
import com.melih.kmptemplate.core.shared.data.internal.remote.RemoteMoviesDataSource
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.network.api.di.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val dataModule = module {
    includes(networkModule)

    single<MoviesRepository> {
        DefaultMoviesRepository(get(named("applicationScope")), get(), get())
    }

    factory<RemoteMoviesDataSource>()
    factory<InMemoryMoviesDataSource>()
}
