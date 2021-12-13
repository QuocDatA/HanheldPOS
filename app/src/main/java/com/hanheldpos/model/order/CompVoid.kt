package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.settings.Reason
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompVoid(
    var CompVoidGuid: String,
    var CompVoidGroupId: Int?,
    var CompVoidTitle: String?,
    var CompVoidValue: Int,
    var CompVoidTotalPrice: Double?
) : Parcelable {
    constructor(src: Reason, parent_id: Int?, totalPrice: Double?) : this(
        CompVoidGuid = src.compVoidGuid,
        CompVoidGroupId = parent_id,
        CompVoidTitle = src.title,
        CompVoidValue = src.compVoidValue,
        CompVoidTotalPrice = totalPrice
    );
}