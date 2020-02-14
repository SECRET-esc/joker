package com.pd.pokerdom

import android.app.Application
import com.pd.pokerdom.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    open fun configureDi() =
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appComponent)
        }
}