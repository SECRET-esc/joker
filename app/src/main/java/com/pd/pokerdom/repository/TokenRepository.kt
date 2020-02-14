package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.model.Token


class TokenRepository(private val api: ApiService) {

    suspend fun sendToken(token: Token) = api.sendToken(token)

}