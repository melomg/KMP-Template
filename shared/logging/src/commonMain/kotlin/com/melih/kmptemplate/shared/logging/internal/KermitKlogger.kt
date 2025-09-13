package com.melih.kmptemplate.shared.logging.internal

import co.touchlab.kermit.Logger
import com.melih.kmptemplate.shared.logging.Klogger
import com.melih.kmptemplate.shared.model.platform.Platform

internal class KermitKlogger(
    private val platform: Platform
) : Klogger {

    override fun isVerboseEnabled(): Boolean = platform.isDebuggable

    override fun verbose(tag: String?, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.v(message = message)
    }

    override fun verbose(tag: String?, throwable: Throwable, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.v(throwable = throwable, message = message)
    }

    override fun isDebugEnabled(): Boolean = platform.isDebuggable

    override fun debug(tag: String?, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.d(message = message)
    }

    override fun debug(tag: String?, throwable: Throwable, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.d(throwable = throwable, message = message)
    }

    override fun info(tag: String?, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.i(message = message)
    }

    override fun info(tag: String?, throwable: Throwable, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.i(throwable = throwable, message = message)
    }

    override fun warn(tag: String?, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.w(message = message)
    }

    override fun warn(tag: String?, throwable: Throwable, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.w(throwable = throwable, message = message)
    }

    override fun error(tag: String?, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.e(message = message)
    }

    override fun error(tag: String?, throwable: Throwable, message: () -> String) {
        Logger.setTag(tag.orDefault())
        Logger.e(throwable = throwable, message = message)
    }

    private fun String?.orDefault() = this ?: DEFAULT_TAG

    private companion object {
        private const val DEFAULT_TAG = "KMPTemplate"
    }
}
