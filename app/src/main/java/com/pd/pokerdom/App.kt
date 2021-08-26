package com.pd.pokerdom

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.pd.pokerdom.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import androidx.lifecycle.Observer


class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        checkConnectionState()
        configureDi()
        configureFirebaseToken()
    }

    private fun configureDi() =
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appComponent)
        }


//    private fun checkConnectionState() {
//        NetworkLiveData.init(this)
//        Log.d("Observer", "State: ${NetworkLiveData.isNetworkAvaiable()}")
//        NetworkLiveData.observeForever {
//            Log.d("Observer", "State: ${it}")
//        }
//    }

    private fun configureFirebaseToken() {
//        val token = FirebaseInstanceId.getInstance().token
//        Log.d("Firebase", "token1 $token")

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d("Firebase", "token ${it.token}")

        }



//       val id =  FirebaseInstallations.getInstance().id
//        Log.d("IDFirebase", "id $id")


//        Thread(Runnable {
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }).start()

    }
}
