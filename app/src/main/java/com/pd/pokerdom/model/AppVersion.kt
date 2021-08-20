package com.pd.pokerdom.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import com.pd.pokerdom.BuildConfig.VERSION_KEY
import com.pd.pokerdom.BuildConfig.VERSION_LIMIT_KEY
import com.pd.pokerdom.BuildConfig.SITE_KEY
import com.pd.pokerdom.BuildConfig.HASH_KEY

@Parcelize
data class AppVersion(
    @SerializedName(VERSION_KEY) var version: String?,
    @SerializedName(HASH_KEY) var hash: String?,
    @SerializedName(VERSION_LIMIT_KEY) val versionLimit: String?,
    @SerializedName(SITE_KEY) val site: String?
): Parcelable