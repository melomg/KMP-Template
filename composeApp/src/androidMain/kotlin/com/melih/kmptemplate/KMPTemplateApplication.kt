package com.melih.kmptemplate

import android.app.Application
import android.util.Log
import com.melih.kmptemplate.di.initKoin
import com.melih.kmptemplate.logging.AndroidLogHelper
import io.github.oshai.kotlinlogging.KotlinLogging

object Static {
    init {
        // Configure kotlin-logging to use Android's native logging
        System.setProperty("kotlin-logging-to-android-native", "true")
    }
}
private val static = Static

class KMPTemplateApplication : Application() {
    companion object {
        private const val TAG = "KMPTemplateApp"
    }
    
    override fun onCreate() {
        super.onCreate()
        initKoin()

        // Initialize static properties
        static
        
        // Configure Android logging
        AndroidLogHelper.configureLogging(this)
        
        // Test Android logging behavior
        LoggingTest.testIsLoggable()
        
        // Test kotlin-logging
        val logger = KotlinLogging.logger {}
        logger.debug { "Test Debug Message" }
        logger.info { "Test Info Message" }
        
        // Direct Android logging
//        Log.d(TAG, "Direct Android debug log")
//        Log.i(TAG, "Direct Android info log")
        
        // Check if logging is enabled after configuration
        val isLoggable = Log.isLoggable(TAG, Log.DEBUG)
        Log.i(TAG, "After configuration: Is $TAG loggable at DEBUG level? $isLoggable")
    }
}
