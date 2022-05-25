package com.hanheldpos.data.api.pojo.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashDrawerStatusResp(
    val CashDrawerGuid: String,
    val StartingCash: Double,
    val StatusId: Int,
    val _key: Int
) : Parcelable