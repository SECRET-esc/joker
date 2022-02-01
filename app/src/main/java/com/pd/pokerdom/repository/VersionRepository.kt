package com.pd.pokerdom.repository

import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.BuildConfig

class VersionRepository(private val api: ApiService) {

    suspend fun getAppVersion() = api.getAppVersion("${BuildConfig.URL_WEBVIEW_VERSION_FILE}play/fs/files/version.json")

}