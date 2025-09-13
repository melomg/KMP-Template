package com.melih.kmptemplate.shared.logging

import com.melih.kmptemplate.shared.logging.FakeKlogger.LogLevel
import kotlinx.coroutines.Runnable
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KlogTest {

    @BeforeTest
    fun setUpAndTearDown() {
        Klog.uprootAll()
    }

    @Test
    fun recursion() {
        val expectedMessage = "Cannot plant Klog into itself."

        assertFailsWith<IllegalArgumentException> { Klog.plant(Klog) }.also {
            assertEquals(expectedMessage, it.message)
        }

        assertFailsWith<IllegalArgumentException> { Klog.plant(*arrayOf(Klog)) }.also {
            assertEquals(expectedMessage, it.message)
        }
    }

    @Test
    fun forestReturnsAllPlanted() {
        val tree1 = FakeKlogger()
        val tree2 = FakeKlogger()
        Klog.plant(tree1)
        Klog.plant(tree2)

        val allLoggers = Klog.allLoggers()

        assertEquals(expected = 2, actual = allLoggers.size)
        assertContains(allLoggers, tree1)
        assertContains(allLoggers, tree2)
    }

    @Test
    fun uprootAllRemovesAll() {
        val klogger1 = FakeKlogger()
        val klogger2 = FakeKlogger()
        Klog.plant(klogger1)
        Klog.plant(klogger2)

        Klog.debug { "First" }
        Klog.uprootAll()
        Klog.debug { "Second" }

        klogger1.assertLog()
            .hasDebugLog("KlogTest", "First")
            .hasNoMoreMessages()

        klogger2.assertLog()
            .hasDebugLog("KlogTest", "First")
            .hasNoMoreMessages()
    }

    internal inner class ThisIsAReallyLongClassName {
        fun run() {
            Klog.debug { "Hello, world!" }
        }
    }

    @Test
    fun debugTreeTagNoTruncation() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        ThisIsAReallyLongClassName().run()

        fakeKlogger.assertLog()
            .hasDebugLog("ThisIsAReallyLongClassName", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun anonymousClassTags() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        object : Runnable {
            override fun run() {
                Klog.debug { "Hello, world!" }

                object : Runnable {
                    override fun run() {
                        Klog.debug { "Hello, world!" }
                    }
                }.run()
            }
        }.run()

        fakeKlogger.assertLog()
            .hasDebugLog("anonymousClassTags$1", "Hello, world!")
            .hasDebugLog("anonymousClassTags$1\$run$2", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun anonymousClassWithInnerSAMLambda() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        object : Runnable {
            override fun run() {
                Klog.debug { "Hello, world!" }

                Runnable { Klog.debug { "Hello, world!" } }.run()
            }
        }.run()

        fakeKlogger.assertLog()
            .hasDebugLog("anonymousClassWithInnerSAMLambda$1", "Hello, world!")
            .hasDebugLog("anonymousClassWithInnerSAMLambda$1", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun anonymousClassMarkerWithOuterSAMLambda() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        Runnable {
            Klog.debug { "Hello, world!" }

            object : Runnable {
                override fun run() {
                    Klog.debug { "Hello, world!" }
                }
            }.run()
        }.run()

        fakeKlogger.assertLog()
            .hasDebugLog("KlogTest", "Hello, world!")
            .hasDebugLog("anonymousClassMarkerWithOuterSAMLambda$1$2", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun anonymousLambdaClassMarker() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        val outer = {
            Klog.debug { "Hello, world!" }

            val inner = {
                Klog.debug { "Hello, world!" }
            }

            inner()
        }

        outer()

        fakeKlogger.assertLog()
            .hasDebugLog("KlogTest", "Hello, world!")
            .hasDebugLog("KlogTest", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun samLambdasUsesClassName() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        Runnable {
            Klog.debug { "Hello, world!" }

            Runnable {
                Klog.debug { "Hello, world!" }
            }.run()
        }.run()

        fakeKlogger.assertLog()
            .hasDebugLog("KlogTest", "Hello, world!")
            .hasDebugLog("KlogTest", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun customTag() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        Klog.debug("Custom") { "Hello, world!" }

        fakeKlogger.assertLog()
            .hasDebugLog(tag = "Custom", "Hello, world!")
            .hasNoMoreMessages()
    }

    @Test
    fun messageWithException() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        val datThrowable = NullPointerException("Message")
        Klog.error(throwable = datThrowable) { "OMFG!" }

        fakeKlogger.assertLog()
            .hasErrorLog(tag = "KlogTest", message = "OMFG!", throwable = datThrowable)
    }

    @Test
    fun exceptionOnly() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        val illegalArgumentException = IllegalArgumentException("Test!")
        val unsupportedOperationException = UnsupportedOperationException("Test!")
        val nullPointerException = NullPointerException("Test!")
        val indexOutOfBoundsException = IndexOutOfBoundsException("Test!")
        val concurrentModificationException = ConcurrentModificationException("Test!")

        Klog.verbose(throwable = illegalArgumentException) { "" }
        Klog.debug(throwable = unsupportedOperationException) { "" }
        Klog.info(throwable = nullPointerException) { "" }
        Klog.warn(throwable = indexOutOfBoundsException) { "" }
        Klog.error(throwable = concurrentModificationException) { "" }

        fakeKlogger.assertLog()
            .hasVerboseLog("KlogTest", "", throwable = illegalArgumentException)
            .hasDebugLog("KlogTest", "", throwable = unsupportedOperationException)
            .hasInfoLog("KlogTest", "", throwable = nullPointerException)
            .hasWarnLog("KlogTest", "", throwable = indexOutOfBoundsException)
            .hasErrorLog("KlogTest", "", throwable = concurrentModificationException)
    }

    @Test
    fun doesNotChunkAcrossNewlinesAndLimit() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        Klog.debug {
            'a'.repeat(3000) + '\n'.toString() +
                    'b'.repeat(6000) + '\n'.toString() +
                    'c'.repeat(3000)
        }

        fakeKlogger.assertLog()
            .hasDebugLog(
                tag = "KlogTest",
                message = 'a'.repeat(3000) + '\n'.toString() +
                        'b'.repeat(6000) + '\n'.toString() +
                        'c'.repeat(3000)
            )
            .hasNoMoreMessages()
    }

    @Test
    fun logAtSpecifiedPriority() {
        val fakeKlogger = FakeKlogger()
        Klog.plant(fakeKlogger)

        Klog.verbose { "Hello, World!" }
        Klog.debug { "Hello, World!" }
        Klog.info { "Hello, World!" }
        Klog.warn { "Hello, World!" }
        Klog.error { "Hello, World!" }

        fakeKlogger.assertLog()
            .hasVerboseLog("KlogTest", "Hello, World!")
            .hasDebugLog("KlogTest", "Hello, World!")
            .hasInfoLog("KlogTest", "Hello, World!")
            .hasWarnLog("KlogTest", "Hello, World!")
            .hasErrorLog("KlogTest", "Hello, World!")
            .hasNoMoreMessages()
    }

    @Test
    fun tagIsClearedWhenNotLoggable() {
        val fakeKlogger = FakeKlogger(isInfoEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.info(tag = "NotLogged") { "Message not logged" }
        Klog.warn { "Message logged" }

        fakeKlogger.assertLog()
            .hasWarnLog("KlogTest", "Message logged")
            .hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenVerboseDisabled() {
        val fakeKlogger = FakeKlogger(isVerboseEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.verbose { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenVerboseDisabled_withThrowable() {
        val fakeKlogger = FakeKlogger(isVerboseEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.verbose(throwable = IllegalStateException("Test!")) { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenDebugDisabled() {
        val fakeKlogger = FakeKlogger(isDebugEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.debug { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenDebugDisabled_withThrowable() {
        val fakeKlogger = FakeKlogger(isDebugEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.debug(throwable = IllegalStateException("Test!")) { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenInfoDisabled() {
        val fakeKlogger = FakeKlogger(isInfoEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.info { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenInfoDisabled_withThrowable() {
        val fakeKlogger = FakeKlogger(isInfoEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.info(throwable = IllegalStateException("Test!")) { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenWarnDisabled() {
        val fakeKlogger = FakeKlogger(isWarnEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.warn { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenWarnDisabled_withThrowable() {
        val fakeKlogger = FakeKlogger(isWarnEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.warn(throwable = IllegalStateException("Test!")) { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenErrorDisabled() {
        val fakeKlogger = FakeKlogger(isErrorEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.error { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    @Test
    fun doesNotLogWhenErrorDisabled_withThrowable() {
        val fakeKlogger = FakeKlogger(isErrorEnabled = false)
        Klog.plant(fakeKlogger)

        Klog.error(throwable = IllegalStateException("Test!")) { "Should not be logged" }

        fakeKlogger.assertLog().hasNoMoreMessages()
    }

    private fun Char.repeat(number: Int) = toString().repeat(number)

    private fun FakeKlogger.assertLog(): LogAssert {
        return LogAssert(logItems)
    }

    private class LogAssert(private val items: List<FakeKlogger.LogItem>) {
        private var index = 0

        fun hasVerboseLog(tag: String, message: String, throwable: Throwable? = null): LogAssert {
            return hasLog(LogLevel.VERBOSE, tag, message, throwable)
        }

        fun hasDebugLog(tag: String, message: String, throwable: Throwable? = null): LogAssert {
            return hasLog(LogLevel.DEBUG, tag, message, throwable)
        }

        fun hasInfoLog(tag: String, message: String, throwable: Throwable? = null): LogAssert {
            return hasLog(LogLevel.INFO, tag, message, throwable)
        }

        fun hasWarnLog(tag: String, message: String, throwable: Throwable? = null): LogAssert {
            return hasLog(LogLevel.WARN, tag, message, throwable)
        }

        fun hasErrorLog(tag: String, message: String, throwable: Throwable? = null): LogAssert {
            return hasLog(LogLevel.ERROR, tag, message, throwable)
        }

        private fun hasLog(
            priority: LogLevel,
            tag: String,
            message: String,
            throwable: Throwable? = null,
        ): LogAssert {
            val item = items[index++]
            assertEquals(expected = priority, actual = item.level)
            assertEquals(expected = tag, actual = item.tag)
            assertEquals(expected = message, actual = item.message())
            assertEquals(expected = throwable, actual = item.throwable)
            return this
        }

        fun hasNoMoreMessages() {
            assertEquals(expected = index, actual = items.size)
        }
    }
}
