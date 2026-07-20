package com.melih.kmptemplate.core.shared.test.util

import com.melih.kmptemplate.core.shared.model.annotations.VisibleForTesting
import com.melih.kmptemplate.core.shared.threading.DispatcherProvider
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.aroundEachTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * A testBalloon [TestConfig] that sets the Main dispatcher to [testDispatcher]
 * for the duration of the test.
 */
@Suppress("VisibleForTests")
@OptIn(VisibleForTesting::class)
fun TestConfig.withMainTestDispatcher(
    testDispatcher: TestDispatcher = StandardTestDispatcher(),
) = aroundEachTest { action ->
    DispatcherProvider.set(
        main = testDispatcher,
        default = testDispatcher,
        io = testDispatcher
    )
    Dispatchers.setMain(testDispatcher)
    try {
        action()
    } finally {
        DispatcherProvider.reset()
        Dispatchers.resetMain()
    }
}
