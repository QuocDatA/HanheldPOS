package com.hanheldpos.data.api.pojo.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashDrawerStatusResp(
    val DidError: Boolean,
    val ErrorMessage: String,
    val Message: String?,
    val Model: List<CashDrawerStatus>
) : Parcelable