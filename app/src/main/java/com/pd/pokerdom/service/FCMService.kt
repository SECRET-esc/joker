package com.pd.pokerdom.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pd.pokerdom.R
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.main.MainActivity
import com.pd.pokerdom.ui.web.WebViewModel
import com.pd.pokerdom.util.applyImageUrl
import com.pd.pokerdom.worker.MyWorker
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
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("Firebase", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("Firebase", "Message Notification Title: ${it.title}")
            Log.d("Firebase", "Message Notification Body: ${it.body}")
            Log.d("Firebase", "Message Notification imageUrl: ${it.imageUrl}")
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

        showNotificationForeground(remoteMessage)
    }

    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(applicationContext).beginWith(work).enqueue()
    }

    private fun sendRegistrationToServer(token: String) {
        val userId = prefs.userId
        val customUserId = prefs.customUserId

        if (!TextUtils.isEmpty(userId) || !TextUtils.isEmpty(customUserId)) {
            viewModel.sentTokenToServer(userId = userId, customUserId = customUserId)
        }
    }

    private fun showNotificationForeground(remoteMessage: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        remoteMessage.data.let {
            if (remoteMessage.data.containsKey(KEY_FCM_LINK)) {
                intent.putExtra(KEY_FCM_LINK, remoteMessage.data[KEY_FCM_LINK])
            }
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = "Default_name"

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmapImage = applyImageUrl(remoteMessage.notification?.imageUrl.toString())

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setLargeIcon(bitmapImage)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmapImage)
                .bigLargeIcon(null))
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, notification.build())
    }
}

