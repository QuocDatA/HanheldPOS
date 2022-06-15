package com.hanheldpos.data.api.pojo.setting.firebase


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseSetting(
    @SerializedName("Environment")
    val environment: String?,
    @SerializedName("FileAuthentication")
    val fileAuthentication: String?,
    @SerializedName("FireStorePath")
    val fireStorePath: FireStorePath?,
    @SerializedName("Path")
    val path: Path?,
    @SerializedName("RootDB")
    val rootDB: String?,
    @SerializedName("UserDB")
    val userDB: String?,
    @SerializedName("UserGuid")
    val userGuid: String?
) : Parcelable