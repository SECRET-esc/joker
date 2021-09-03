package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.model.TokenObj
import com.pd.pokerdom.BuildConfig

class TokenRepository(private val api: ApiService) {

    suspend fun sendToken(tokenObj: TokenObj) = api.sendToken(tokenObj, "${BuildConfig.URL_HOST_TOKEN}/api/webhook/push/android")

}