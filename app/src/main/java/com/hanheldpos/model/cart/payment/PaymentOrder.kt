package com.hanheldpos.model.cart.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentOrder(
    var _id: String,
    var ApplyToId: Int,
    var PaymentTypeId: Int,
    var Title: String,
    var Payable: Double,
    var OverPay: Double,
    var EmployeeName: String,
    var CardNo: String,
//    var SubId: Int,
    // var VoucherList:
    var CreateDate: String
) : Parcelable {

}