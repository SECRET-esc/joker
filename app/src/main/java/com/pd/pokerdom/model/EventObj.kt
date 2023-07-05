package com.pd.pokerdom.model

import com.google.gson.annotations.SerializedName

data class EventObj(
    @SerializedName("event") var event: String = "click",
    @SerializedName("type") var type: String,
    @SerializedName("eventContext") val eventContext: EventContext,
)

data class EventContext(
    @SerializedName("userId") val userId: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("defaultLink") val defaultLink: String,
    @SerializedName("userAgent") val userAgent: String,
    @SerializedName("action") val action: String,
)