package com.melih.kmptemplate.shared.logging

/** A facade for handling logging calls. Install instances via [`Klog.plant()`][.plant]. */
@Suppress("TooManyFunctions")
interface Klogger {

    fun isVerboseEnabled(): Boolean = true

    /** Lazy add a log message if isVerboseEnabled is true */
    fun verbose(tag: String? = null, message: () -> String)

    /** Lazy add a log message if isVerboseEnabled is true */
    fun verbose(tag: String? = null, throwable: Throwable, message: () -> String)

    fun isDebugEnabled(): Boolean = true

    /** Lazy add a log message if isDebugEnabled is true */
    fun debug(tag: String? = null, message: () -> String)

    /** Lazy add a log message if isDebugEnabled is true */
    fun debug(tag: String? = null, throwable: Throwable, message: () -> String)

    fun isInfoEnabled(): Boolean = true

    /** Lazy add a log message if isInfoEnabled is true */
    fun info(tag: String? = null, message: () -> String)

    /** Lazy add a log message if isInfoEnabled is true */
    fun info(tag: String? = null, throwable: Throwable, message: () -> String)

    fun isWarnEnabled(): Boolean = true

    /** Lazy add a log message if isWarnEnabled is true */
    fun warn(tag: String? = null, message: () -> String)

    /** Lazy add a log message if isWarnEnabled is true */
    fun warn(tag: String? = null, throwable: Throwable, message: () -> String)

    fun isErrorEnabled(): Boolean = true

    /** Lazy add a log message if isErrorEnabled is true */
    fun error(tag: String? = null, message: () -> String)

    /** Lazy add a log message if isErrorEnabled is true */
    fun error(tag: String? = null, throwable: Throwable, message: () -> String)
}
