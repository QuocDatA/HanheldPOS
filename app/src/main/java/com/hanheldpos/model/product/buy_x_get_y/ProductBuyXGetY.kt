package com.hanheldpos.model.product.buy_x_get_y

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductBuyXGetY(
    val ApplyToModifier: Int,
    val CategoryGuid: String,
    val Color: String,
    val Description1: String,
    val Description2: String,
    val Discount: String?,
    val Handle: String,
    val IsLock: Boolean,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val MaxAmount: Int,
    val MaxQuantity: Int,
    val Name1: String,
    val Name2: String,
    val Name3: String,
    val Name4: String,
    val Parent_id: String?,
    val Price: Double,
    val Price1: Double,
    val Price2: Double,
    val ProductTypeId: Int,
    val Sku: String,
    val UnitType: Int,
    val Url: String,
    val Variant: String?,
    val _id: String,
) : Parcelable