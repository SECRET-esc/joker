package com.pd.pokerdom.api

import com.pd.pokerdom.model.Token
import retrofit2.http.POST


interface ApiService {

    @POST()
    suspend fun sendToken(token: Token): String

}