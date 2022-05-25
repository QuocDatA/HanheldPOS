package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentsOrderStatus(
    val Id: Int,
    val ListDicOrderStatus: List<DicOrderStatus>?,
    val Title_en: String
) : Parcelable {
    fun findTextPaymentStatus(paymentStatusId : Int) :String? {
        return ListDicOrderStatus?.firstOrNull{status -> status.OrderStatusId == paymentStatusId}?.Title_en;
    }

    fun findColorPaymentStatus(paymentStatusId : Int) : String? {
        return ListDicOrderStatus?.firstOrNull{status -> status.OrderStatusId == paymentStatusId}?.Color;
    }
}