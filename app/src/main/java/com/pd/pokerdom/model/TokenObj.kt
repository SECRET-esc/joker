package com.pd.pokerdom.model

import com.google.gson.annotations.SerializedName

data class TokenObj(
    @SerializedName("user_id") var userId: String?,
    @SerializedName("custom_user_id") var customUserId: String?,
    @SerializedName("token") val token: String,
    @SerializedName("time") val dateTime: Long = System.currentTimeMillis(),
    @SerializedName("type") val type: String = "android"
    ) {
}