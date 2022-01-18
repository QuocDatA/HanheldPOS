package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.fee.ChooseProductApplyTo
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerGets(
    val ApplyTo: Int,
    val CustomerName: String,
    val DiscountValue: Double,
    val DiscountValueType: Int,
    val Handle: String,
    val ListApplyTo: List<Product>,
    val ProductApplyTo: ChooseProductApplyTo?,
    val Quantity: Int
) : Parcelable