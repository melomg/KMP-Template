package com.melih.kmptemplate.core.shared.test.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

fun TestScope.launchInBackground(
    block: suspend CoroutineScope.() -> Unit
) = backgroundScope.launch(context = UnconfinedTestDispatcher(testScheduler), block = block)
    .also {
        advanceUntilIdle()
    }
