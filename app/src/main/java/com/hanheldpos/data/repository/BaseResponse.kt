package com.hanheldpos.data.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseResponse<T> : Parcelable {
    @SerializedName("message")
    val message: String = ""

    @SerializedName("success")
    val success: Boolean = false

    @SerializedName("data")
    val responseData: T? = null
}
