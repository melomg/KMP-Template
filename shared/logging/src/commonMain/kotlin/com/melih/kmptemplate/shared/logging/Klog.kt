package com.melih.kmptemplate.shared.logging

class Klog private constructor() {
    init {
        throw AssertionError()
    }

    companion object Forest : Klogger() {
        /** Log a verbose message with optional format args. */
        override fun v(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.v(message, *args) }
        }

        /** Log a verbose exception and a message with optional format args. */
        override fun v(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.v(t, message, *args) }
        }

        /** Log a verbose exception. */
        override fun v(t: Throwable?) {
            loggerArray.forEach { it.v(t) }
        }

        /** Log a debug message with optional format args. */
        override fun d(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.d(message, *args) }
        }

        /** Log a debug exception and a message with optional format args. */
        override fun d(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.d(t, message, *args) }
        }

        /** Log a debug exception. */
        override fun d(t: Throwable?) {
            loggerArray.forEach { it.d(t) }
        }

        /** Log an info message with optional format args. */
        override fun i(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.i(message, *args) }
        }

        /** Log an info exception and a message with optional format args. */
        override fun i(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.i(t, message, *args) }
        }

        /** Log an info exception. */
        override fun i(t: Throwable?) {
            loggerArray.forEach { it.i(t) }
        }

        /** Log a warning message with optional format args. */
        override fun w(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.w(message, *args) }
        }

        /** Log a warning exception and a message with optional format args. */
        override fun w(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.w(t, message, *args) }
        }

        /** Log a warning exception. */
        override fun w(t: Throwable?) {
            loggerArray.forEach { it.w(t) }
        }

        /** Log an error message with optional format args. */
        override fun e(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.e(message, *args) }
        }

        /** Log an error exception and a message with optional format args. */
        override fun e(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.e(t, message, *args) }
        }

        /** Log an error exception. */
        override fun e(t: Throwable?) {
            loggerArray.forEach { it.e(t) }
        }

        /** Log an assert message with optional format args. */
        override fun wtf(message: String?, vararg args: Any?) {
            loggerArray.forEach { it.wtf(message, *args) }
        }

        /** Log an assert exception and a message with optional format args. */
        override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.wtf(t, message, *args) }
        }

        /** Log an assert exception. */
        override fun wtf(t: Throwable?) {
            loggerArray.forEach { it.wtf(t) }
        }

        /** Log at `priority` a message with optional format args. */
        override fun log(priority: Int, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.log(priority, message, *args) }
        }

        /** Log at `priority` an exception and a message with optional format args. */
        override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            loggerArray.forEach { it.log(priority, t, message, *args) }
        }

        /** Log at `priority` an exception. */
        override fun log(priority: Int, t: Throwable?) {
            loggerArray.forEach { it.log(priority, t) }
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError() // Missing override for log method.
        }

        /**
         * A view into Klog's planted loggers as a logger itself. This can be used for injecting a logger
         * instance rather than using static methods or to facilitate testing.
         */
        @Suppress(
            "NOTHING_TO_INLINE", // Kotlin users should reference `Klogger.Forest` directly.
            "NON_FINAL_MEMBER_IN_OBJECT" // For japicmp check.
        )
        open inline fun asKlogger(): Klogger = this

        /** Set a one-time tag for use on the next logging call. */
        fun tag(tag: String): Klogger {
            for (logger in loggerArray) {
                logger.explicitTag.set(tag)
            }
            return this
        }

        /** Add a new logging logger. */
        fun plant(logger: Klogger) {
            require(logger !== this) { "Cannot plant Klog into itself." }
            synchronized(loggers) {
                loggers.add(logger)
                loggerArray = loggers.toTypedArray()
            }
        }

        /** Adds new logging loggers. */
        fun plant(vararg loggers: Klogger) {
            for (logger in loggers) {
                requireNotNull(logger) { "loggers contained null" }
                require(logger !== this) { "Cannot plant Klog into itself." }
            }
            synchronized(this.loggers) {
                Collections.addAll(this.loggers, *loggers)
                loggerArray = this.loggers.toTypedArray()
            }
        }

        /** Remove a planted logger. */
        fun uproot(logger: Klogger) {
            synchronized(loggers) {
                require(loggers.remove(logger)) { "Cannot uproot logger which is not planted: $logger" }
                loggerArray = loggers.toTypedArray()
            }
        }

        /** Remove all planted loggers. */
        fun uprootAll() {
            synchronized(loggers) {
                loggers.clear()
                loggerArray = emptyArray()
            }
        }

        /** Return a copy of all planted [loggers][Klogger]. */
        fun forest(): List<Klogger> {
            synchronized(loggers) {
                return unmodifiableList(loggers.toList())
            }
        }

        val loggerCount get() = loggerArray.size

        // Both fields guarded by 'loggers'.
        private val loggers = ArrayList<Klogger>()
        @Volatile private var loggerArray = emptyArray<Klogger>()
    }
}
