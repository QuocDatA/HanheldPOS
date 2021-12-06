package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompVoid(
    var CompVoidGuid: String,
    var CompVoidGroupId: Int?,
    var CompVoidTitle: String?,
    var CompVoidValue: Int,
    var CompVoidTotalPrice: Double?
) : Parcelable {
}