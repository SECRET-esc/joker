package com.pd.pokerdom.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

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