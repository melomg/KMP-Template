package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.internal.DefaultMuseumRepository
import com.melih.kmptemplate.core.shared.data.internal.InMemoryMuseumStorage
import com.melih.kmptemplate.core.shared.data.internal.MuseumStorage
import com.melih.kmptemplate.core.shared.domain.api.MuseumRepository
import com.melih.kmptemplate.core.shared.network.api.MuseumApi
import com.melih.kmptemplate.core.shared.network.api.di.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)

    single<MuseumStorage> { InMemoryMuseumStorage() }
    single<MuseumRepository> {
        DefaultMuseumRepository(get(named("applicationScope")), get<MuseumApi>(), get())
    }
}
