package com.melih.kmptemplate.shared.logging

class FakeKlogger(
    private val isVerboseEnabled: Boolean = true,
    private val isDebugEnabled: Boolean = true,
    private val isInfoEnabled: Boolean = true,
    private val isWarnEnabled: Boolean = true,
    private val isErrorEnabled: Boolean = true,
) : Klogger {
    val logItems = mutableListOf<LogItem>()

    override fun isVerboseEnabled(): Boolean = isVerboseEnabled
    override fun isDebugEnabled(): Boolean = isDebugEnabled
    override fun isInfoEnabled(): Boolean = isInfoEnabled
    override fun isWarnEnabled(): Boolean = isWarnEnabled
    override fun isErrorEnabled(): Boolean = isErrorEnabled

    override fun verbose(tag: String?, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.VERBOSE, message))
    }

    override fun verbose(tag: String?, throwable: Throwable, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.VERBOSE, message, throwable))
    }

    override fun debug(tag: String?, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.DEBUG, message))
    }

    override fun debug(tag: String?, throwable: Throwable, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.DEBUG, message, throwable))
    }

    override fun info(tag: String?, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.INFO, message))
    }

    override fun info(tag: String?, throwable: Throwable, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.INFO, message, throwable))
    }

    override fun warn(tag: String?, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.WARN, message))
    }

    override fun warn(tag: String?, throwable: Throwable, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.WARN, message, throwable))
    }

    override fun error(tag: String?, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.ERROR, message))
    }

    override fun error(tag: String?, throwable: Throwable, message: () -> String) {
        logItems.add(LogItem(tag ?: TAG, LogLevel.ERROR, message, throwable))
    }

    data class LogItem(
        val tag: String?,
        val level: LogLevel,
        val message: () -> String,
        val throwable: Throwable? = null
    )

    enum class LogLevel {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }

    private companion object {
        private const val TAG = "FakeKlogger"
    }
}
