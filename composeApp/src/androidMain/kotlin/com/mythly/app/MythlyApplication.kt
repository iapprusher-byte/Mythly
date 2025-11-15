package com.mythly.app

import android.app.Application
import com.mythly.app.di.getAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MythlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MythlyApplication)
            modules(getAppModules())
        }
    }
}
