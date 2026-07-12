package com.melih.kmptemplate.di

import com.melih.kmptemplate.BuildKonfig
import com.melih.kmptemplate.core.shared.data.api.di.dataModule
import com.melih.kmptemplate.core.shared.logging.Klog
import com.melih.kmptemplate.core.shared.logging.getKloggers
import com.melih.kmptemplate.core.shared.model.platform.BuildType
import com.melih.kmptemplate.core.shared.model.platform.Platform
import com.melih.kmptemplate.core.shared.threading.DispatcherProvider
import com.melih.kmptemplate.features.museum.api.di.museumFeatureModule
import com.melih.kmptemplate.features.settings.api.di.settingsFeatureModule
import com.melih.kmptemplate.platform.platformVersionName
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val appModule = module {
    single(named("applicationScope")) {
        CoroutineScope(
            DispatcherProvider.Main + SupervisorJob() + CoroutineExceptionHandler { _, error ->
                Klog.error(
                    tag = "ApplicationScope",
                    throwable = error
                ) { "Application scope error" }
            }
        )
    }
}

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

fun initKoin() {
    val koinApp = startKoin {
        modules(
            appModule,
            platformModule,
            dataModule,
            museumFeatureModule,
            settingsFeatureModule,
        )
    }

    val platform = koinApp.koin.get<Platform>()
    Klog.plant(loggers = getKloggers(platform))
    Klog.debug { "Koin is initialized for platform $platform" }
}
