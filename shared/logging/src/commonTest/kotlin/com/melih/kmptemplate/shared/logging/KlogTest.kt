package com.melih.kmptemplate.shared.logging

class KlogTest {
//
//    @BeforeTest
//    fun setUpAndTearDown() {
//        Klog.uprootAll()
//    }
//
//    @Test
//    fun debugTreeCanAlterCreatedTag() {
//        Klog.plant(FakeKlogger)
//
//        Klog.debug { "Test" }
//
//        assertLog()
//            .hasDebugMessage("TimberTest:38", "Test")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun recursion() {
//        val timber = Klog.asTree()
//
//        assertThrows<IllegalArgumentException> {
//            Klog.plant(timber)
//        }.hasMessageThat().isEqualTo("Cannot plant Timber into itself.")
//
//        assertThrows<IllegalArgumentException> {
//            @Suppress("RemoveRedundantSpreadOperator") // Explicitly calling vararg overload.
//            Klog.plant(*arrayOf(timber))
//        }.hasMessageThat().isEqualTo("Cannot plant Timber into itself.")
//    }
//
//    @Test
//    fun treeCount() {
//        // inserts trees and checks if the amount of returned trees matches.
//        assertThat(Klog.treeCount).isEqualTo(0)
//        for (i in 1 until 50) {
//            Klog.plant(Klog.DebugTree())
//            assertThat(Klog.treeCount).isEqualTo(i)
//        }
//        Klog.uprootAll()
//        assertThat(Klog.treeCount).isEqualTo(0)
//    }
//
//    @Test
//    fun forestReturnsAllPlanted() {
//        val tree1 = Klog.DebugTree()
//        val tree2 = Klog.DebugTree()
//        Klog.plant(tree1)
//        Klog.plant(tree2)
//
//        assertThat(Klog.forest()).containsExactly(tree1, tree2)
//    }
//
//    @Test
//    fun forestReturnsAllTreesPlanted() {
//        val tree1 = Klog.DebugTree()
//        val tree2 = Klog.DebugTree()
//        Klog.plant(tree1, tree2)
//
//        assertThat(Klog.forest()).containsExactly(tree1, tree2)
//    }
//
//    @Test
//    fun uprootThrowsIfMissing() {
//        assertThrows<IllegalArgumentException> {
//            Klog.uproot(Klog.DebugTree())
//        }.hasMessageThat().startsWith("Cannot uproot tree which is not planted: ")
//    }
//
//    @Test
//    fun uprootRemovesTree() {
//        val tree1 = Klog.DebugTree()
//        val tree2 = Klog.DebugTree()
//        Klog.plant(tree1)
//        Klog.plant(tree2)
//        Klog.d("First")
//        Klog.uproot(tree1)
//        Klog.d("Second")
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "First")
//            .hasDebugMessage("TimberTest", "First")
//            .hasDebugMessage("TimberTest", "Second")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun uprootAllRemovesAll() {
//        val tree1 = Klog.DebugTree()
//        val tree2 = Klog.DebugTree()
//        Klog.plant(tree1)
//        Klog.plant(tree2)
//        Klog.d("First")
//        Klog.uprootAll()
//        Klog.d("Second")
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "First")
//            .hasDebugMessage("TimberTest", "First")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun noArgsDoesNotFormat() {
//        Klog.plant(Klog.DebugTree())
//        Klog.d("te%st")
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "te%st")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun debugTreeTagGeneration() {
//        Klog.plant(Klog.DebugTree())
//        Klog.d("Hello, world!")
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    internal inner class ThisIsAReallyLongClassName {
//        fun run() {
//            Klog.d("Hello, world!")
//        }
//    }
//
//    @Config(sdk = [25])
//    @Test
//    fun debugTreeTagTruncation() {
//        Klog.plant(Klog.DebugTree())
//
//        ThisIsAReallyLongClassName().run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest\$ThisIsAReall", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Config(sdk = [26])
//    @Test
//    fun debugTreeTagNoTruncation() {
//        Klog.plant(Klog.DebugTree())
//
//        ThisIsAReallyLongClassName().run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest\$ThisIsAReallyLongClassName", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Suppress("ObjectLiteralToLambda") // Lambdas != anonymous classes.
//    @Test
//    fun debugTreeTagGenerationStripsAnonymousClassMarker() {
//        Klog.plant(Klog.DebugTree())
//        object : Runnable {
//            override fun run() {
//                Klog.d("Hello, world!")
//
//                object : Runnable {
//                    override fun run() {
//                        Klog.d("Hello, world!")
//                    }
//                }.run()
//            }
//        }.run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest\$debugTreeTag", "Hello, world!")
//            .hasDebugMessage("TimberTest\$debugTreeTag", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Suppress("ObjectLiteralToLambda") // Lambdas != anonymous classes.
//    @Test
//    fun debugTreeTagGenerationStripsAnonymousClassMarkerWithInnerSAMLambda() {
//        Klog.plant(Klog.DebugTree())
//        object : Runnable {
//            override fun run() {
//                Klog.d("Hello, world!")
//
//                Runnable { Klog.d("Hello, world!") }.run()
//            }
//        }.run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest\$debugTreeTag", "Hello, world!")
//            .hasDebugMessage("TimberTest\$debugTreeTag", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Suppress("ObjectLiteralToLambda") // Lambdas != anonymous classes.
//    @Test
//    fun debugTreeTagGenerationStripsAnonymousClassMarkerWithOuterSAMLambda() {
//        Klog.plant(Klog.DebugTree())
//
//        Runnable {
//            Klog.d("Hello, world!")
//
//            object : Runnable {
//                override fun run() {
//                    Klog.d("Hello, world!")
//                }
//            }.run()
//        }.run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasDebugMessage("TimberTest\$debugTreeTag", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun debugTreeTagGenerationStripsAnonymousLambdaClassMarker() {
//        Klog.plant(Klog.DebugTree())
//
//        val outer = {
//            Klog.d("Hello, world!")
//
//            val inner = {
//                Klog.d("Hello, world!")
//            }
//
//            inner()
//        }
//
//        outer()
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun debugTreeTagGenerationForSAMLambdasUsesClassName() {
//        Klog.plant(Klog.DebugTree())
//
//        Runnable {
//            Klog.d("Hello, world!")
//
//            Runnable {
//                Klog.d("Hello, world!")
//            }.run()
//        }.run()
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasDebugMessage("TimberTest", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    private class ClassNameThatIsReallyReallyReallyLong {
//        init {
//            Klog.i("Hello, world!")
//        }
//    }
//
//    @Test
//    fun debugTreeGeneratedTagIsLoggable() {
//        Klog.plant(object : Klog.DebugTree() {
//            private val MAX_TAG_LENGTH = 23
//
//            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//                try {
//                    assertTrue(Log.isLoggable(tag, priority))
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                        assertTrue(tag!!.length <= MAX_TAG_LENGTH)
//                    }
//                } catch (e: IllegalArgumentException) {
//                    fail(e.message)
//                }
//
//                super.log(priority, tag, message, t)
//            }
//        })
//        ClassNameThatIsReallyReallyReallyLong()
//        assertLog()
//            .hasInfoMessage("TimberTest\$ClassNameTha", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun debugTreeCustomTag() {
//        Klog.plant(Klog.DebugTree())
//        Klog.tag("Custom").d("Hello, world!")
//
//        assertLog()
//            .hasDebugMessage("Custom", "Hello, world!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun messageWithException() {
//        Klog.plant(Klog.DebugTree())
//        val datThrowable = truncatedThrowable(NullPointerException::class.java)
//        Klog.e(datThrowable, "OMFG!")
//
//        assertExceptionLogged(Log.ERROR, "OMFG!", "java.lang.NullPointerException")
//    }
//
//    @Test
//    fun exceptionOnly() {
//        Klog.plant(Klog.DebugTree())
//
//        Klog.v(truncatedThrowable(IllegalArgumentException::class.java))
//        assertExceptionLogged(
//            Log.VERBOSE,
//            null,
//            "java.lang.IllegalArgumentException",
//            "TimberTest",
//            0
//        )
//
//        Klog.i(truncatedThrowable(NullPointerException::class.java))
//        assertExceptionLogged(Log.INFO, null, "java.lang.NullPointerException", "TimberTest", 1)
//
//        Klog.d(truncatedThrowable(UnsupportedOperationException::class.java))
//        assertExceptionLogged(
//            Log.DEBUG, null, "java.lang.UnsupportedOperationException", "TimberTest",
//            2
//        )
//
//        Klog.w(truncatedThrowable(UnknownHostException::class.java))
//        assertExceptionLogged(Log.WARN, null, "java.net.UnknownHostException", "TimberTest", 3)
//
//        Klog.e(truncatedThrowable(ConnectException::class.java))
//        assertExceptionLogged(Log.ERROR, null, "java.net.ConnectException", "TimberTest", 4)
//
//        Klog.wtf(truncatedThrowable(AssertionError::class.java))
//        assertExceptionLogged(Log.ASSERT, null, "java.lang.AssertionError", "TimberTest", 5)
//    }
//
//    @Test
//    fun exceptionOnlyCustomTag() {
//        Klog.plant(Klog.DebugTree())
//
//        Klog.tag("Custom").v(truncatedThrowable(IllegalArgumentException::class.java))
//        assertExceptionLogged(Log.VERBOSE, null, "java.lang.IllegalArgumentException", "Custom", 0)
//
//        Klog.tag("Custom").i(truncatedThrowable(NullPointerException::class.java))
//        assertExceptionLogged(Log.INFO, null, "java.lang.NullPointerException", "Custom", 1)
//
//        Klog.tag("Custom").d(truncatedThrowable(UnsupportedOperationException::class.java))
//        assertExceptionLogged(
//            Log.DEBUG,
//            null,
//            "java.lang.UnsupportedOperationException",
//            "Custom",
//            2
//        )
//
//        Klog.tag("Custom").w(truncatedThrowable(UnknownHostException::class.java))
//        assertExceptionLogged(Log.WARN, null, "java.net.UnknownHostException", "Custom", 3)
//
//        Klog.tag("Custom").e(truncatedThrowable(ConnectException::class.java))
//        assertExceptionLogged(Log.ERROR, null, "java.net.ConnectException", "Custom", 4)
//
//        Klog.tag("Custom").wtf(truncatedThrowable(AssertionError::class.java))
//        assertExceptionLogged(Log.ASSERT, null, "java.lang.AssertionError", "Custom", 5)
//    }
//
//    @Test
//    fun exceptionFromSpawnedThread() {
//        Klog.plant(Klog.DebugTree())
//        val datThrowable = truncatedThrowable(NullPointerException::class.java)
//        val latch = CountDownLatch(1)
//        object : Thread() {
//            override fun run() {
//                Klog.e(datThrowable, "OMFG!")
//                latch.countDown()
//            }
//        }.start()
//        latch.await()
//        assertExceptionLogged(
//            Log.ERROR,
//            "OMFG!",
//            "java.lang.NullPointerException",
//            "TimberTest\$exceptionFro"
//        )
//    }
//
//    @Test
//    fun nullMessageWithThrowable() {
//        Klog.plant(Klog.DebugTree())
//        val datThrowable = truncatedThrowable(NullPointerException::class.java)
//        Klog.e(datThrowable, null)
//
//        assertExceptionLogged(Log.ERROR, "", "java.lang.NullPointerException")
//    }
//
//    @Test
//    fun chunkAcrossNewlinesAndLimit() {
//        Klog.plant(Klog.DebugTree())
//        Klog.d(
//            'a'.repeat(3000) + '\n'.toString() + 'b'.repeat(6000) + '\n'.toString() + 'c'.repeat(
//                3000
//            )
//        )
//
//        assertLog()
//            .hasDebugMessage("TimberTest", 'a'.repeat(3000))
//            .hasDebugMessage("TimberTest", 'b'.repeat(4000))
//            .hasDebugMessage("TimberTest", 'b'.repeat(2000))
//            .hasDebugMessage("TimberTest", 'c'.repeat(3000))
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun nullMessageWithoutThrowable() {
//        Klog.plant(Klog.DebugTree())
//        Klog.d(null as String?)
//
//        assertLog().hasNoMoreMessages()
//    }
//
//    @Test
//    fun logMessageCallback() {
//        val logs = ArrayList<String>()
//        Klog.plant(object : Klog.DebugTree() {
//            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//                logs.add("$priority $tag $message")
//            }
//        })
//
//        Klog.v("Verbose")
//        Klog.tag("Custom").v("Verbose")
//        Klog.d("Debug")
//        Klog.tag("Custom").d("Debug")
//        Klog.i("Info")
//        Klog.tag("Custom").i("Info")
//        Klog.w("Warn")
//        Klog.tag("Custom").w("Warn")
//        Klog.e("Error")
//        Klog.tag("Custom").e("Error")
//        Klog.wtf("Assert")
//        Klog.tag("Custom").wtf("Assert")
//
//        assertThat(logs).containsExactly( //
//            "2 TimberTest Verbose", //
//            "2 Custom Verbose", //
//            "3 TimberTest Debug", //
//            "3 Custom Debug", //
//            "4 TimberTest Info", //
//            "4 Custom Info", //
//            "5 TimberTest Warn", //
//            "5 Custom Warn", //
//            "6 TimberTest Error", //
//            "6 Custom Error", //
//            "7 TimberTest Assert", //
//            "7 Custom Assert" //
//        )
//    }
//
//    @Test
//    fun logAtSpecifiedPriority() {
//        Klog.plant(Klog.DebugTree())
//
//        Klog.log(Log.VERBOSE, "Hello, World!")
//        Klog.log(Log.DEBUG, "Hello, World!")
//        Klog.log(Log.INFO, "Hello, World!")
//        Klog.log(Log.WARN, "Hello, World!")
//        Klog.log(Log.ERROR, "Hello, World!")
//        Klog.log(Log.ASSERT, "Hello, World!")
//
//        assertLog()
//            .hasVerboseMessage("TimberTest", "Hello, World!")
//            .hasDebugMessage("TimberTest", "Hello, World!")
//            .hasInfoMessage("TimberTest", "Hello, World!")
//            .hasWarnMessage("TimberTest", "Hello, World!")
//            .hasErrorMessage("TimberTest", "Hello, World!")
//            .hasAssertMessage("TimberTest", "Hello, World!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun formatting() {
//        Klog.plant(Klog.DebugTree())
//        Klog.v("Hello, %s!", "World")
//        Klog.d("Hello, %s!", "World")
//        Klog.i("Hello, %s!", "World")
//        Klog.w("Hello, %s!", "World")
//        Klog.e("Hello, %s!", "World")
//        Klog.wtf("Hello, %s!", "World")
//
//        assertLog()
//            .hasVerboseMessage("TimberTest", "Hello, World!")
//            .hasDebugMessage("TimberTest", "Hello, World!")
//            .hasInfoMessage("TimberTest", "Hello, World!")
//            .hasWarnMessage("TimberTest", "Hello, World!")
//            .hasErrorMessage("TimberTest", "Hello, World!")
//            .hasAssertMessage("TimberTest", "Hello, World!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun isLoggableControlsLogging() {
//        Klog.plant(object : Klog.DebugTree() {
//            @Suppress("OverridingDeprecatedMember") // Explicitly testing deprecated variant.
//            override fun isLoggable(priority: Int): Boolean {
//                return priority == Log.INFO
//            }
//        })
//        Klog.v("Hello, World!")
//        Klog.d("Hello, World!")
//        Klog.i("Hello, World!")
//        Klog.w("Hello, World!")
//        Klog.e("Hello, World!")
//        Klog.wtf("Hello, World!")
//
//        assertLog()
//            .hasInfoMessage("TimberTest", "Hello, World!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun isLoggableTagControlsLogging() {
//        Klog.plant(object : Klog.DebugTree() {
//            override fun isLoggable(tag: String?, priority: Int): Boolean {
//                return "FILTER" == tag
//            }
//        })
//        Klog.tag("FILTER").v("Hello, World!")
//        Klog.d("Hello, World!")
//        Klog.i("Hello, World!")
//        Klog.w("Hello, World!")
//        Klog.e("Hello, World!")
//        Klog.wtf("Hello, World!")
//
//        assertLog()
//            .hasVerboseMessage("FILTER", "Hello, World!")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun logsUnknownHostExceptions() {
//        Klog.plant(Klog.DebugTree())
//        Klog.e(truncatedThrowable(UnknownHostException::class.java), null)
//
//        assertExceptionLogged(Log.ERROR, "", "UnknownHostException")
//    }
//
//    @Test
//    fun tagIsClearedWhenNotLoggable() {
//        Klog.plant(object : Klog.DebugTree() {
//            override fun isLoggable(tag: String?, priority: Int): Boolean {
//                return priority >= Log.WARN
//            }
//        })
//        Klog.tag("NotLogged").i("Message not logged")
//        Klog.w("Message logged")
//
//        assertLog()
//            .hasWarnMessage("TimberTest", "Message logged")
//            .hasNoMoreMessages()
//    }
//
//    @Test
//    fun logsWithCustomFormatter() {
//        Klog.plant(object : Klog.DebugTree() {
//            override fun formatMessage(message: String, vararg args: Any?): String {
//                return String.format("Test formatting: $message", *args)
//            }
//        })
//        Klog.d("Test message logged. %d", 100)
//
//        assertLog()
//            .hasDebugMessage("TimberTest", "Test formatting: Test message logged. 100")
//    }
//
//    private fun <T : Throwable> truncatedThrowable(throwableClass: Class<T>): T {
//        val throwable = throwableClass.newInstance()
//        val stackTrace = throwable.stackTrace
//        val traceLength = if (stackTrace.size > 5) 5 else stackTrace.size
//        throwable.stackTrace = stackTrace.copyOf(traceLength)
//        return throwable
//    }
//
//    private fun Char.repeat(number: Int) = toString().repeat(number)
//
//    private fun assertExceptionLogged(
//        logType: Int,
//        message: String?,
//        exceptionClassname: String,
//        tag: String? = null,
//        index: Int = 0
//    ) {
//        val logs = getLogs()
//        assertThat(logs).hasSize(index + 1)
//        val log = logs[index]
//        assertThat(log.type).isEqualTo(logType)
//        assertThat(log.tag).isEqualTo(tag ?: "TimberTest")
//
//        if (message != null) {
//            assertThat(log.msg).startsWith(message)
//        }
//
//        assertThat(log.msg).contains(exceptionClassname)
//        // We use a low-level primitive that Robolectric doesn't populate.
//        assertThat(log.throwable).isNull()
//    }
//
//    private fun assertLog(): LogAssert {
//        return LogAssert(getLogs())
//    }
//
//    private fun getLogs() = ShadowLog.getLogs().filter { it.tag != ROBOLECTRIC_INSTRUMENTATION_TAG }
//
//    private inline fun <reified T : Throwable> assertThrows(body: () -> Unit): ThrowableSubject {
//        try {
//            body()
//        } catch (t: Throwable) {
//            if (t is T) {
//                return assertThat(t)
//            }
//            throw t
//        }
//        throw AssertionError("Expected body to throw ${T::class.java.name} but completed successfully")
//    }
//
//    private class LogAssert internal constructor(private val items: List<FakeKlogger.LogItem>) {
//        private var index = 0
//
//        fun hasVerboseMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.VERBOSE, tag, message)
//        }
//
//        fun hasDebugMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.DEBUG, tag, message)
//        }
//
//        fun hasInfoMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.INFO, tag, message)
//        }
//
//        fun hasWarnMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.WARN, tag, message)
//        }
//
//        fun hasErrorMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.ERROR, tag, message)
//        }
//
//        fun hasAssertMessage(tag: String, message: String): LogAssert {
//            return hasMessage(Log.ASSERT, tag, message)
//        }
//
//        private fun hasMessage(priority: Int, tag: String, message: String): LogAssert {
//            val item = items[index++]
//            assertThat(item.level).isEqualTo(priority)
//            assertThat(item.tag).isEqualTo(tag)
//            assertThat(item.msg).isEqualTo(message)
//            return this
//        }
//
//        fun hasNoMoreMessages() {
//            assertThat(items).hasSize(index)
//        }
//    }
//
//    private companion object Companion {
//        private const val ROBOLECTRIC_INSTRUMENTATION_TAG = "MonitoringInstr"
//    }
}
