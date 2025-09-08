package com.melih.kmptemplate.shared.logging

import com.melih.kmptemplate.shared.model.platform.Platform
import io.sentry.kotlin.multiplatform.Sentry

internal class SentryKlogger(
    private val platform: Platform
) : Klogger {

    override fun isVerboseEnabled(): Boolean = platform.isDebuggable

    override fun verbose(tag: String?, message: () -> String) {
        // no-op
    }

    override fun verbose(tag: String?, throwable: Throwable, message: () -> String) {
        Sentry.captureException(throwable)
    }

    override fun isDebugEnabled(): Boolean = platform.isDebuggable

    override fun debug(tag: String?, message: () -> String) {
        // no-op
    }

    override fun debug(tag: String?, throwable: Throwable, message: () -> String) {
        Sentry.captureException(throwable)
    }

    override fun info(tag: String?, message: () -> String) {
        Sentry.captureMessage(message())
    }

    override fun info(tag: String?, throwable: Throwable, message: () -> String) {
        Sentry.captureException(throwable)
    }

    override fun warn(tag: String?, message: () -> String) {
        Sentry.captureMessage(message())
    }

    override fun warn(tag: String?, throwable: Throwable, message: () -> String) {
        Sentry.captureException(throwable)
    }

    override fun error(tag: String?, message: () -> String) {
        Sentry.captureMessage(message())
    }

    override fun error(tag: String?, throwable: Throwable, message: () -> String) {
        Sentry.captureException(throwable)
    }
}
