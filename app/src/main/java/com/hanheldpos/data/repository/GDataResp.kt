package com.hanheldpos.data.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GDataResp<T : Parcelable>(
    @SerializedName("Message")
    val message: String? = "",
    @SerializedName("Model")
    val model: List<T> = listOf(),
    @SerializedName("ErrorMessage")
    val errorMessage: String? = "",
    @SerializedName("DidError")
    val didError: Boolean? = true
) : Parcelable
