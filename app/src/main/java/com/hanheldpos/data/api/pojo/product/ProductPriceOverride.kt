package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductPriceOverride(
    val LocationGuid: String,
    val Price: Double,
    val VariantPriceOverrideList: List<VariantPriceOverride>?= null,
) : Parcelable