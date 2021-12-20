package com.hanheldpos.data.api.pojo.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashDrawerStatus(
    val CashDrawerGuid: String,
    val StartingCash: Int,
    val StatusId: Int,
    val _key: Int
) : Parcelable