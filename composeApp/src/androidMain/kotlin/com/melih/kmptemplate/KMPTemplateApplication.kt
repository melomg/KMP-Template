package com.melih.kmptemplate

import android.app.Application
import com.melih.kmptemplate.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class KMPTemplateApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.INFO)
            androidContext(this@KMPTemplateApplication)
        }
    }
}
