package com.melih.kmptemplate.core.shared.test.utils

import com.melih.kmptemplate.core.shared.data.api.di.dataTestModule
import com.melih.kmptemplate.core.shared.model.platform.BuildType
import com.melih.kmptemplate.core.shared.model.platform.Platform
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.aroundEachTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module

private val platformTestModule = module {
    single<Platform> {
        Platform(
            appName = "TestApp",
            appVersionCode = 1,
            appVersionName = "1.0.0",
            platformVersionName = "TestPlatform",
            effectiveBuildType = BuildType.DEBUG,
            isDebuggable = true,
        )
    }
}

fun TestConfig.withKoin() = aroundEachTest { action ->
    val koinApp = koinApplication {
        modules(
            platformTestModule,
            dataTestModule,
        )
    }
    startKoin(koinApp)
    try {
        action()
    } finally {
        stopKoin()
    }
}
