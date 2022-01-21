package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.fee.ChooseProductApplyTo
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerBuys(
    val ApplyTo: Int,
    val CustomerName: String,
    val Handle: String,
    val ListApplyTo: List<Product>,
    val MinimumTypeId: Int,
    val MinimumValue: Double,
    val MinimumValueFormat: String?,
    val ProductApplyTo: ChooseProductApplyTo?
) : Parcelable