package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantPriceOverrideListItem(
    val GroupName: String?,
    val VariationName: String?,
    val VariationSku: String?,
    val UnitTypeId: Int?,
    val Price: Double?,
    val PriceOverride: String?,
    val ProductGuid: String?,
) : Parcelable
