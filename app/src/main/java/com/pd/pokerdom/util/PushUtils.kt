package com.pd.pokerdom.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.pd.pokerdom.R
import com.pd.pokerdom.service.FCMService.Companion.KEY_FCM_LINK
import com.pd.pokerdom.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

fun showNotification(
    context: Context,
    title: String?,
    body: String?,
    imageLink: String? = null,
    webLink: String? = null
) {

    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    webLink.let {
        intent.putExtra(KEY_FCM_LINK, webLink)
    }

    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    val channelId = context.getString(R.string.default_notification_channel_id)
    val channelName = "Default_name"

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val bitmapImage = applyImageUrl(imageLink)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_logo)
        .setContentTitle(title)
        .setContentText(body)
        .setColor(ContextCompat.getColor(context, R.color.colorIco))
        .setDefaults(Notification.DEFAULT_ALL)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    bitmapImage?.let {
        notification
            .setLargeIcon(bitmapImage)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmapImage)
                    .bigLargeIcon(null)
            )
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
    manager.notify(0, notification.build())
}

fun applyImageUrl(imageUrl: String?): Bitmap? = runBlocking {
    withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val input = url.openStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }?.let { bitmap -> return@let bitmap }
}