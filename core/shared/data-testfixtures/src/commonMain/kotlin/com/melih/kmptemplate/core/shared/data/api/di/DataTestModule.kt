package com.melih.kmptemplate.core.shared.data.api.di

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.domain.api.MoviesRepository
import com.melih.kmptemplate.core.shared.threading.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.bind
import org.koin.dsl.module

val dataTestModule = module {
    single { TestMoviesRepository() } bind MoviesRepository::class
    single(Qualifiers.APPLICATION_SCOPE) {
        CoroutineScope(DispatcherProvider.Default + SupervisorJob())
    }
}
