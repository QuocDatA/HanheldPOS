package com.hanheldpos.model.discount

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiscountItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountServer(
    val AllowQuantity : Int,
    val AllowAmount : Double?,
    val DiscountInfo : DiscountItem
) : Parcelable {

}