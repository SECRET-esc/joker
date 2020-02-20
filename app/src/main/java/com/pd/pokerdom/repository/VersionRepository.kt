package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService


class VersionRepository(private val api: ApiService) {

    suspend fun getAppVersion() = api.getAppVersion()

}