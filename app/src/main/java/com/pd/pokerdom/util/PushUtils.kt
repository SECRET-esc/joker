package com.pd.pokerdom.util

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.pd.pokerdom.R
import com.pd.pokerdom.model.EventContext
import com.pd.pokerdom.model.EventObj
import com.pd.pokerdom.service.FCMService.Companion.DATA_KEY
import com.pd.pokerdom.service.FCMService.Companion.KEY_FCM_LINK
import com.pd.pokerdom.service.FCMService.Companion.KEY_FROM_NOTIFICATION
import com.pd.pokerdom.ui.ApplicationState
import com.pd.pokerdom.ui.main.MainActivity
import com.pd.pokerdom.ui.start.StartActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL

@SuppressLint("UnspecifiedImmutableFlag")
fun showNotification(
    context: Context,
    title: String?,
    body: String?,
    imageLink: String? = null,
    webLink: String? = null,
    data: Map<String, String>,
) {

    val intent = Intent(context, StartActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtra(DATA_KEY, Gson().toJson(makeClass(type = data["eventFrom"], subject = data["title"], userId = data["userId"], defaultLink = data["link"], userAgent = "")))
    Log.d("FirebaseMessage", "website: $webLink")
    webLink.let {
        Log.d("FirebaseMessage", "State behaviour subject before: ${ApplicationState().getApplicationState().value}")
        ApplicationState().fromNotification()
        Log.d("FirebaseMessage", "State behaviour subject after: ${ApplicationState().getApplicationState().value}")
        val link = webLink?.replace(" ", "")
        intent.putExtra(KEY_FCM_LINK, link)
        intent.putExtra(KEY_FROM_NOTIFICATION, true)
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

fun makeClass(type: String?, subject: String?, userId: String?, defaultLink: String?, userAgent: String?): EventObj {
    return EventObj(type = type!!, eventContext = EventContext(subject = subject!!, userId = userId!!, defaultLink = defaultLink!!, action = "open", userAgent = userAgent!!))
}