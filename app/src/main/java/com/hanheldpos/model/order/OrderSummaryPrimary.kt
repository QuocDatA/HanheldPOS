package com.hanheldpos.model.order

import android.content.Context
import android.os.Parcelable
import com.hanheldpos.R
import com.hanheldpos.utils.DateTimeUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSummaryPrimary(
    val _id : String?= null,
    val _key : String?= null,
    val Source : Int?= null,
    val LocationName : String? = null,
    val CustomerGuid : String?= null,
    val CustomerFullName : String? = null,
    val EmployeeGuid : String?= null,
    val EmployeeFullName : String?= null,
    val Acronymn : String?= null,
    val EmployeeAvatar : String?= null,
    val OrderStatusText : String?= null,
    val OrderStatusColor : String?= null,
    val OrderCode: String? = null,
    val OrderStatusId: Int?= null,
    val OrderDescription : String?= null,
    val PaymentStatusId: Int?= null,
    val PaymentStatusText : String?= null,
    val PaymentStatusColor : String?= null,
    val DeliveryCode : String?= null,
    val GrandTotal: Double?= null,
    val TableId: String?= null,
    val TableName : String?= null,
    val TableTypeId : Int?= null,
    val DiningOptionId: Int?= null,
    val DiningOptionName: String?= null,
    val DiningOptionOrderNo : Int?= null,
    val DateText : String?= null,
    val TimeText : String?= null,
    val DateValue : String?= null,
    val TimeValue : String?= null,
    val IsNow : Boolean? = false,
    val OrderWait : Int? = null,
    val EstimateTime : Int? = null,
    val CurrencyId : Int?= null,
    val CurrencySymbol : String?= null,
    val OrderCreateDate : String?= null,
    val IsNew : Boolean? = false,
) : Parcelable {
    @IgnoredOnParcel
    var synced: Boolean? = null
    fun groupOrderName(context: Context) : String {
        return if (IsNew == true){
            context.getString(R.string.new_orders)
        } else DiningOptionName?: ""

    }
    val groupHistoryName :String get() =  DateTimeUtils.strToStr(OrderCreateDate,DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS,DateTimeUtils.Format.EEEE_dd_MMM_yyyy)
}