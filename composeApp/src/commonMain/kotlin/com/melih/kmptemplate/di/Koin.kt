package com.melih.kmptemplate.di

import com.melih.kmptemplate.data.InMemoryMuseumStorage
import com.melih.kmptemplate.data.MuseumRepository
import com.melih.kmptemplate.data.MuseumStorage
import com.melih.kmptemplate.screens.detail.DetailViewModel
import com.melih.kmptemplate.screens.list.ListViewModel
import com.melih.kmptemplate.shared.network.MuseumApi
import com.melih.kmptemplate.shared.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)

    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get<MuseumApi>(), get()).apply {
            initialize()
        }
    }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
