package com.melih.kmptemplate.shared.logging.internal

import com.melih.kmptemplate.shared.logging.Klogger
import com.melih.kmptemplate.shared.model.platform.Platform
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Suppress("TooManyFunctions")
internal class NapierKlogger(
    private val platform: Platform
) : Klogger {

    init {
        Napier.base(DebugAntilog())
    }

    override fun isVerboseEnabled(): Boolean = platform.isDebuggable

    override fun verbose(tag: String?, message: () -> String) {
        Napier.v(tag = tag.orDefault(), message = message)
    }

    override fun verbose(tag: String?, throwable: Throwable, message: () -> String) {
        Napier.v(tag = tag.orDefault(), message = message, throwable = throwable)
    }

    override fun isDebugEnabled(): Boolean = platform.isDebuggable

    override fun debug(tag: String?, message: () -> String) {
        Napier.d(tag = tag.orDefault(), message = message)
    }

    override fun debug(tag: String?, throwable: Throwable, message: () -> String) {
        Napier.d(tag = tag.orDefault(), message = message, throwable = throwable)
    }

    override fun info(tag: String?, message: () -> String) {
        Napier.i(tag = tag.orDefault(), message = message)
    }

    override fun info(tag: String?, throwable: Throwable, message: () -> String) {
        Napier.i(tag = tag.orDefault(), message = message, throwable = throwable)
    }

    override fun warn(tag: String?, message: () -> String) {
        Napier.w(tag = tag.orDefault(), message = message)
    }

    override fun warn(tag: String?, throwable: Throwable, message: () -> String) {
        Napier.w(tag = tag.orDefault(), message = message, throwable = throwable)
    }

    override fun error(tag: String?, message: () -> String) {
        Napier.e(tag = tag.orDefault(), message = message)
    }

    override fun error(tag: String?, throwable: Throwable, message: () -> String) {
        Napier.e(tag = tag.orDefault(), message = message, throwable = throwable)
    }

    private fun String?.orDefault() = this ?: DEFAULT_TAG

    private companion object {
        private const val DEFAULT_TAG = "KMPTemplate"
    }
}
