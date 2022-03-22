package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantPriceOverride(
    val Sku: String?,
    val Price: Double?,
) : Parcelable
