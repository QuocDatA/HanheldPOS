package com.hanheldpos.data.api.pojo.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateCashDrawerResp(
    val CashDrawerGuid: String,
    val Code: String,
    val EndCash: Double,
    val StatusId: Int
) : Parcelable