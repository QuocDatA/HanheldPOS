package com.hanheldpos.data.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BaseResponse<T> (
    val Message: String?,
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Model: T?,
)
