package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.model.TokenObj


class TokenRepository(private val api: ApiService) {

    suspend fun sendToken(tokenObj: TokenObj) = api.sendToken(tokenObj)

}