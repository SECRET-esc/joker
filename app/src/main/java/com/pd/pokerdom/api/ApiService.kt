package com.pd.pokerdom.api

import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.model.EventObj
import com.pd.pokerdom.model.TokenObj
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET // GET Version
    suspend fun getAppVersion(@Url url: String): AppVersion
//"
    @POST
    suspend fun sendToken(@Body tokenObj: TokenObj, @Url url: String)

    @POST
    suspend fun sendEvent(@Body eventObj: EventObj, @Url url: String)
}