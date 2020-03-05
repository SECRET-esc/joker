package com.pd.pokerdom.service

import android.text.TextUtils
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.main.MainActivity
import com.pd.pokerdom.ui.web.WebViewModel
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
        }

        remoteMessage.data.let {
            if (it.isNotEmpty()) {
                Log.d("Firebase", "Message data payload: $it")
                if (it.containsKey(KEY_CONFIG_DOMAIN)) {
                    prefs.configDomain = it[KEY_CONFIG_DOMAIN].toString()
                }
                if (it.containsKey(KEY_FCM_LINK)) {
                    Log.d("Firebase", "KEY_LINK: ${it[KEY_FCM_LINK]}")
                    MainActivity.open(this)
                }
            }
        }
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


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private fun sendNotification(messageBody: String) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT)
//        val channelId = getString(R.string.default_notification_channel_id)
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,
//                    "YOUR_CHANNEL_NAME",
//                    NotificationManager.IMPORTANCE_DEFAULT)
//            channel.description = "YOUR_NOTIFICATION_CHANNEL_DISCRIPTION"
//            notificationManager.createNotificationChannel(channel)
//        }
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = Notification.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getString(R.string.fcm_message))
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
//    }
}