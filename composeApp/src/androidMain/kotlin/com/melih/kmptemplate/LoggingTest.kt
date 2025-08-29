package com.melih.kmptemplate

import android.util.Log

/**
 * This class is used to test Android logging behavior
 */
object LoggingTest {
    private const val TAG = "LoggingTest"
    
    fun testIsLoggable() {
        // Check if the tag is loggable at DEBUG level
        val isLoggable = Log.isLoggable(TAG, Log.DEBUG)
        
        // Log the result
        Log.i(TAG, "Is $TAG loggable at DEBUG level? $isLoggable")
        
        // Try to log a debug message
        Log.d(TAG, "This is a debug message that may not appear")
        
        // Log an info message which should appear by default
        Log.i(TAG, "This is an info message that should appear")
    }
}