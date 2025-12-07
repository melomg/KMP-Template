package com.melih.kmptemplate.core.android.threading.test.util

import android.annotation.SuppressLint
import com.melih.kmptemplate.core.shared.model.annotations.VisibleForTesting
import com.melih.kmptemplate.core.shared.threading.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@SuppressLint("VisibleForTests")
@Suppress("unused")
@OptIn(VisibleForTesting::class, ExperimentalCoroutinesApi::class)
class TestDispatcherExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        val testDispatcher = StandardTestDispatcher()
        DispatcherProvider.set(
            main = testDispatcher,
            default = testDispatcher,
            io = testDispatcher
        )
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext) {
        DispatcherProvider.reset()
        Dispatchers.resetMain()
    }
}
