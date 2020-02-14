package com.pd.pokerdom.api

import com.pd.pokerdom.model.Token
import retrofit2.http.POST


interface ApiService {

    @POST("api/webhook/push/android")
    suspend fun sendToken(token: Token): String

}