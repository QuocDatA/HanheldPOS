package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantsPriceOverrideLocation(
    val LocationGuid: String?,
    val Price: Double?,
) : Parcelable
