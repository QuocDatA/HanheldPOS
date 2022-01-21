package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeeResp(
    val DiscountList: List<Discount>,
    val Fees: List<Fee>,
    val FeesType: Map<String, String>,
) : Parcelable