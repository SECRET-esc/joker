package com.pd.pokerdom.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppVersion(
    @SerializedName("version") var version: String?,
    @SerializedName("hash") var hash: String?,
    @SerializedName("versionLimit") val versionLimit: String?
): Parcelable