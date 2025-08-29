package com.melih.kmptemplate.logging

import android.content.Context
import android.util.Log
import com.melih.kmptemplate.BuildKonfig

/**
 * Helper class for Android logging configuration
 * 
 * This class addresses the issue where android.util.Log.isLoggable() returns false
 * for DEBUG level logs by default, even in debug builds.
 * 
 * The solution is to set system properties for specific tags to enable debug logging.
 * 
 * For production builds, you can also enable debug logging via ADB:
 * ```
 * adb shell setprop log.tag.YOUR_TAG DEBUG
 * ```
 */
object AndroidLogHelper {
    private const val TAG = "AndroidLogHelper"
    
    // List of tags to enable debug logging for
    private val DEBUG_TAGS = listOf(
        "LoggingTest",
        "KMPTemplateApp",
        TAG,
        // Add more tags here as needed
        "kotlin-logging-android" // This is used by kotlin-logging library
    )
    
    /**
     * Configure Android logging based on build type and settings
     * 
     * @param context Application context
     */
    fun configureLogging(context: Context) {
        // Check if we're in a debuggable build
        val isDebuggable = BuildKonfig.IS_DEBUGGABLE
        
        if (isDebuggable) {
            // For debuggable builds, enable all logging
            enableDebugLogging()
        }
        
        // Log the configuration
        Log.i(TAG, "Logging configured. Debug build: $isDebuggable")
    }
    
    /**
     * Enable debug logging for the application
     * 
     * This works by setting the log level property in system properties
     * which affects android.util.Log.isLoggable() behavior
     * 
     * Note: This only works for debug builds on most devices. On production builds,
     * changing system properties requires root access or ADB shell commands.
     */
    private fun enableDebugLogging() {
        try {
            // Enable debug logging for all specified tags
            DEBUG_TAGS.forEach { tagName ->
                val propName = "log.tag.$tagName"
                System.setProperty(propName, "DEBUG")
                
                // Verify if it worked
                val isLoggable = Log.isLoggable(tagName, Log.DEBUG)
                Log.i(TAG, "Tag '$tagName' debug logging enabled: $isLoggable")
            }
            
            Log.i(TAG, "Debug logging enabled for application tags")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable debug logging", e)
        }
    }
    
    /**
     * Check if a specific tag is loggable at the given level
     * 
     * @param tag The log tag to check
     * @param level The log level to check (e.g., Log.DEBUG, Log.INFO)
     * @return true if the tag is loggable at the given level
     */
    fun isLoggable(tag: String, level: Int): Boolean {
        return Log.isLoggable(tag, level)
    }
}