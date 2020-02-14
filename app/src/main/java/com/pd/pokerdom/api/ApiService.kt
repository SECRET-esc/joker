package com.pd.pokerdom.api

import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.model.Token
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @GET("https://android.g2slt.com/play/tr/assets/version.json")
    suspend fun getAppVersion(): AppVersion

    @POST("api/webhook/push/android")
    suspend fun sendToken(token: Token): String

}