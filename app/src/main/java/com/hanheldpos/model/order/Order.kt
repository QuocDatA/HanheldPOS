package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val OrderStatusId : Int?,
    val PaymentStatusId: Int?,
    val UserGuid : String,
    val EmployeeGuid :String,
    val LocationGuid: String,
    val DeviceGuid: String,
    val DiningOptionId : Int?,
    val Code : String?,
    val CreateDate: String?,
    val MenuLocationGuid: String?,
    val CashDrawer_id: String?,
    val CurrencySymbol : String,
) : Parcelable {

}
