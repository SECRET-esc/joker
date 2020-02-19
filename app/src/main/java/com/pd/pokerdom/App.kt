package com.pd.pokerdom

import android.app.Application
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.pd.pokerdom.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
        configureFirebaseToken()
    }



    private fun configureDi() =
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appComponent)
        }

    private fun configureFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d("Firebase", "token " + it.token)
        }
    }
}