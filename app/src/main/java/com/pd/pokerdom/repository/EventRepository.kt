package com.pd.pokerdom.repository

import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.api.ApiService
import com.pd.pokerdom.model.EventObj

class EventRepository(private val api: ApiService) {
    suspend fun sendEvent(eventObj: EventObj) = api.sendEvent(eventObj, "${BuildConfig.URL_HOST_TOKEN}/play/s/api/webhook/push/event")
}