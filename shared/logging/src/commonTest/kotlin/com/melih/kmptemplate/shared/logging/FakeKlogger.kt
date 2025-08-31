package com.melih.kmptemplate.shared.logging

object FakeKlogger : Klogger {
    val logItems = mutableListOf<LogItem>()

    override fun verbose(tag: String?, message: () -> String) {
        logItems.add(LogItem(TAG, LogLevel.VERBOSE, message))
    }

    override fun verbose(tag: String?, throwable: Throwable?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, throwable: Throwable?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun info(tag: String?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun info(tag: String?, throwable: Throwable?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun warn(tag: String?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun warn(tag: String?, throwable: Throwable?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, message: () -> String) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, throwable: Throwable?, message: () -> String) {
        TODO("Not yet implemented")
    }

    data class LogItem(
        val tag: String?,
        val level: LogLevel,
        val message: () -> String,
        val throwable: Throwable? = null
    )

    enum class LogLevel(val priority: Int) {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        ASSERT(7),
    }

    private const val TAG = "FakeKlogger"
}
