package com.pd.pokerdom.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("user_id") var userId: String,
    @SerializedName("custom_user_id") var customUserId: String,
    @SerializedName("token") val currentToken: String?,
    @SerializedName("time") val dateTime: String?,
    @SerializedName("type") val type: String = "android"
    ) {
}