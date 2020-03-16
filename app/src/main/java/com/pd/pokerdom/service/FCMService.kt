package com.pd.pokerdom.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.web.WebViewModel
import com.pd.pokerdom.util.showNotification
import org.koin.android.ext.android.inject


class FCMService : FirebaseMessagingService() {

    companion object {
        private const val KEY_CONFIG_DOMAIN = "config_domain"
        const val KEY_FCM_LINK = "link"
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
        Log.d("Firebase", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("Firebase", "Message Notification Title: ${it.title}")
            Log.d("Firebase", "Message Notification Body: ${it.body}")
            Log.d("Firebase", "Message Notification imageUrl: ${it.imageUrl}")

            showNotification(
                context = this,
                title = remoteMessage.notification?.title,
                body = remoteMessage.notification?.body,
                imageLink = remoteMessage.notification?.imageUrl?.toString(),
                webLink = remoteMessage.data[KEY_FCM_LINK]
            )
        }

        remoteMessage.data.let {
            if (it.isNotEmpty()) {
                Log.d("Firebase", "Message Data: $it")
                if (it.containsKey(KEY_CONFIG_DOMAIN)) {
                    prefs.configDomain = it[KEY_CONFIG_DOMAIN].toString()
                }
                if (it.containsKey(KEY_FCM_LINK)) {
//                    Log.d("Firebase", "KEY_LINK: ${it[KEY_FCM_LINK]}")
//                    MainActivity.open(this, it[KEY_FCM_LINK])
                }
            }
        }
    }

    private fun sendRegistrationToServer(/*token: String*/) {
        val userId = prefs.userId
        val customUserId = prefs.customUserId

        if (!userId.isNullOrEmpty() || !customUserId.isNullOrEmpty() ) {
            viewModel.sentTokenToServer(userId = userId, customUserId = customUserId)
        }
    }
}