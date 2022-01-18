package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Condition(
    val AppliesTo: Int,
    val CustomerBuys: CustomerBuys,
    val CustomerGets: CustomerGets,
    val DiscountGuid: String,
    val DiscountType: Int,
    val DiscountValue: Double,
    val IsDiscountLimit: Int,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val ListApplyTo: List<Product>,
    val ListFromItem: String,
    val MaximumDiscount: Double,
    val MaximumNumberOfUses: Int,
    val MaximumNumberOfUsesValue: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable