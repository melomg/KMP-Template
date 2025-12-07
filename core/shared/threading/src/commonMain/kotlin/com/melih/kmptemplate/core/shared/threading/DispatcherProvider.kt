package com.melih.kmptemplate.core.shared.threading

import com.melih.kmptemplate.core.shared.model.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

expect val ioDispatcher: CoroutineDispatcher

object DispatcherProvider {

    private val DEFAULTS = object {
        val Main = Dispatchers.Main
        val Default = Dispatchers.Default
        val IO = ioDispatcher
    }

    var Main: CoroutineDispatcher = DEFAULTS.Main
        private set

    var Default = DEFAULTS.Default
        private set

    var IO = DEFAULTS.IO
        private set

    @VisibleForTesting
    fun set(
        main: CoroutineDispatcher = DEFAULTS.Main,
        default: CoroutineDispatcher = DEFAULTS.Default,
        io: CoroutineDispatcher = DEFAULTS.IO,
    ) {
        Main = main
        Default = default
        IO = io
    }

    @VisibleForTesting
    fun reset() {
        Main = DEFAULTS.Main
        Default = DEFAULTS.Default
        IO = DEFAULTS.IO
    }
}
