package com.pd.pokerdom.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pd.pokerdom.model.EventContext
import com.pd.pokerdom.model.EventObj
import com.pd.pokerdom.repository.EventRepository
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.main.MainActivity
import com.pd.pokerdom.ui.web.WebViewModel
import com.pd.pokerdom.util.showNotification
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class FCMService : FirebaseMessagingService() {

    companion object {
        private const val KEY_CONFIG_DOMAIN = "config_domain"
        const val KEY_FCM_LINK = "link"
        const val KEY_FROM_NOTIFICATION = "fromnotification"
        const val DATA_KEY = "data"
    }

    private val prefs: SharedPrefsManager by inject()
    private val viewModel: WebViewModel by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Firebase", "Refreshed token: $token")

        prefs.tokenFCM = token
        sendRegistrationToServer(/*token*/)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FirebaseMessage", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("FirebaseMessage", "Message Notification Title: ${it.title}")
            Log.d("FirebaseMessage", "Message Notification Body: ${it.body}")
            Log.d("FirebaseMessage", "Message Notification imageUrl: ${it.imageUrl}")
            Log.d("FirebaseMessage", "Message payload: ${remoteMessage.data}")
            showNotification(
                context = this,
                title = remoteMessage.notification?.title,
                body = remoteMessage.notification?.body,
                imageLink = remoteMessage.notification?.imageUrl?.toString(),
                webLink = remoteMessage.data[KEY_FCM_LINK],
                data = remoteMessage.data,
            )
            Log.d("FirebaseMessage", "Data: ${remoteMessage.data}")
        }

//        remoteMessage.data.let {
//            if (it.isNotEmpty()) {
//                Log.d("FirebaseMessage", "Message Data: $it")
//                if (it.containsKey(KEY_CONFIG_DOMAIN)) {
//                    prefs.configDomain = it[KEY_CONFIG_DOMAIN].toString()
//                }
//                if (it.containsKey(KEY_FCM_LINK)) {
//                    Log.d("FirebaseMessage", "KEY_LINK: ${it[KEY_FCM_LINK]}")
//                    MainActivity.open(this, it[KEY_FCM_LINK])
//                }
//            }
//        }
    }

    private fun sendRegistrationToServer(/*token: String*/) {
        val userId = prefs.userId
        val customUserId = prefs.customUserId

        if (!userId.isNullOrEmpty() || !customUserId.isNullOrEmpty() ) {
            viewModel.sentTokenToServer(userId = userId, customUserId = customUserId)
        }
    }
}