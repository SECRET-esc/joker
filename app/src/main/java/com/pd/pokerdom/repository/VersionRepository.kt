package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.model.Token


class VersionRepository(private val api: ApiService) {

    suspend fun getAppVersion() = api.getAppVersion()

}