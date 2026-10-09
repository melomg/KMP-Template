package com.melih.kmptemplate.core.shared.test.utils

import com.melih.kmptemplate.core.shared.data.api.di.dataTestModule
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.aroundEachTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication

fun TestConfig.withKoin() = aroundEachTest { action ->
    val koinApp = koinApplication {
        modules(dataTestModule)
    }
    startKoin(koinApp)
    try {
        action()
    } finally {
        stopKoin()
    }
}
