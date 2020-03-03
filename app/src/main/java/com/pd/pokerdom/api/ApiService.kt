package com.pd.pokerdom.api

import com.pd.pokerdom.BuildConfig.URL_HOST_TOKEN
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.model.TokenObj
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("play/tr/assets/version.json") // GET Version
    suspend fun getAppVersion(): AppVersion

    @POST("${URL_HOST_TOKEN}api/webhook/push/android") // POST Token (prod)
    suspend fun sendToken(@Body tokenObj: TokenObj)

}