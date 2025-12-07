package com.melih.kmptemplate.core.shared.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Use Default.limitedParallelism as a safe substitute for IO
actual val ioDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default.limitedParallelism(60)
