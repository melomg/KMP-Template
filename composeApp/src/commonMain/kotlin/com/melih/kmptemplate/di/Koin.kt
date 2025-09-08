package com.melih.kmptemplate.di

import com.melih.kmptemplate.BuildKonfig
import com.melih.kmptemplate.data.InMemoryMuseumStorage
import com.melih.kmptemplate.data.MuseumRepository
import com.melih.kmptemplate.data.MuseumStorage
import com.melih.kmptemplate.platform.platformVersionName
import com.melih.kmptemplate.screens.detail.DetailViewModel
import com.melih.kmptemplate.screens.list.ListViewModel
import com.melih.kmptemplate.shared.logging.Klog
import com.melih.kmptemplate.shared.logging.getKloggers
import com.melih.kmptemplate.shared.model.platform.BuildType
import com.melih.kmptemplate.shared.model.platform.Platform
import com.melih.kmptemplate.shared.network.MuseumApi
import com.melih.kmptemplate.shared.network.di.networkModule
import io.sentry.kotlin.multiplatform.Sentry
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val platformModule = module {
    single<Platform> {
        Platform(
            appName = BuildKonfig.APP_NAME,
            appVersionCode = BuildKonfig.VERSION_CODE,
            appVersionName = BuildKonfig.VERSION_NAME,
            platformVersionName = platformVersionName,
            effectiveBuildType = BuildType.byKey(BuildKonfig.EFFECTIVE_BUILD_TYPE),
            isDebuggable = BuildKonfig.IS_DEBUGGABLE,
        )
    }
}

private val dataModule = module {
    includes(networkModule)

    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get<MuseumApi>(), get()).apply {
            initialize()
        }
    }
}

private val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin() {
    val koinApp = startKoin {
        modules(
            platformModule,
            dataModule,
            viewModelModule,
        )
    }

    val platform = koinApp.koin.get<Platform>()

    initSentry(platform)
    Klog.plant(loggers = getKloggers(platform))
}

private fun initSentry(platform: Platform) {
    Sentry.init { options ->
        options.dsn = BuildKonfig.SENTRY_DSN
        options.debug = true
        options.environment = platform.effectiveBuildType.key
    }
}
