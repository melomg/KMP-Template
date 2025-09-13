package com.melih.kmptemplate.shared.logging.internal

import com.melih.kmptemplate.shared.logging.Klogger
import com.melih.kmptemplate.shared.model.platform.Platform
import io.sentry.kotlin.multiplatform.Sentry

internal class SentryKlogger(
    private val platform: Platform
) : Klogger {

    init {
        initSentry()
    }

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

    private fun initSentry() {
        Sentry.init { options ->
            options.dsn = platform.sentryDSN
            options.debug = platform.isDebuggable
            options.environment = platform.effectiveBuildType.key
            options.release = "${platform.appVersionName} (${platform.appVersionCode})"
            options.tracesSampleRate = 1.0
        }

        Sentry.configureScope { scope ->
            scope.setTag("app.name", platform.appName)
            scope.setTag("app.version_name", platform.appVersionName)
            scope.setTag("app.version_code", platform.appVersionCode.toString())
            scope.setTag("build.type", platform.effectiveBuildType.key)
            scope.setTag("platform.version_name", platform.platformVersionName)
        }
    }
}