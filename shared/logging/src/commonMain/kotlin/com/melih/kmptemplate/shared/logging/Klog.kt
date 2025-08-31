package com.melih.kmptemplate.shared.logging

import kotlin.concurrent.Volatile

class Klog private constructor() {
    init {
        throw AssertionError()
    }

    companion object CompositeKlogger : Klogger {

        override fun verbose(tag: String?, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isVerboseEnabled() }
                .forEach { it.verbose(tag = message.resolveTagName(), message) }
        }

        override fun verbose(tag: String?, throwable: Throwable, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isVerboseEnabled() }
                .forEach { it.verbose(tag = message.resolveTagName(), message) }
        }

        override fun debug(tag: String?, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isDebugEnabled() }
                .forEach { it.debug(tag = message.resolveTagName(), message) }
        }

        override fun debug(tag: String?, throwable: Throwable, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isDebugEnabled() }
                .forEach { it.debug(tag = message.resolveTagName(), throwable, message) }
        }

        override fun info(tag: String?, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isInfoEnabled() }
                .forEach { it.info(tag = message.resolveTagName(), message) }
        }

        override fun info(tag: String?, throwable: Throwable, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isInfoEnabled() }
                .forEach { it.info(tag = message.resolveTagName(), throwable, message) }
        }

        override fun warn(tag: String?, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isWarnEnabled() }
                .forEach { it.warn(tag = message.resolveTagName(), message) }
        }

        override fun warn(tag: String?, throwable: Throwable, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isWarnEnabled() }
                .forEach { it.warn(tag = message.resolveTagName(), throwable, message) }
        }

        override fun error(tag: String?, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isErrorEnabled() }
                .forEach { it.error(tag = message.resolveTagName(), message) }
        }

        override fun error(tag: String?, throwable: Throwable, message: () -> String) {
            loggerArray
                .filter { logger -> logger.isErrorEnabled() }
                .forEach { it.error(tag = message.resolveTagName(), throwable, message) }
        }

        /** Add a new logging logger. */
        fun plant(logger: Klogger) {
            require(logger !== this) { "Cannot plant Klog into itself." }
            loggers.add(logger)
            loggerArray = loggers.toTypedArray()
        }

        /** Adds new loggers. */
        fun plant(vararg loggers: Klogger) {
            for (logger in loggers) {
                requireNotNull(logger) { "loggers contained null" }
                require(logger !== this) { "Cannot plant Klog into itself." }
            }
            this.loggers.addAll(loggers)
            loggerArray = this.loggers.toTypedArray()
        }

        /** Remove a planted logger. */
        fun uproot(logger: Klogger) {
            require(loggers.remove(logger)) { "Cannot uproot logger which is not planted: $logger" }
            loggerArray = loggers.toTypedArray()
        }

        /** Remove all planted loggers. */
        fun uprootAll() {
            loggers.clear()
            loggerArray = emptyArray()
        }

        // Both fields guarded by 'loggers'.
        private val loggers = ArrayList<Klogger>()

        @Volatile
        private var loggerArray = emptyArray<Klogger>()
    }
}
