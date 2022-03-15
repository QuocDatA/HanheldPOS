package com.hanheldpos.model.cart.payment

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.payment.Voucher
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentOrder(
    var _id: String?,
    var ApplyToId: Int?,
    var PaymentTypeId: Int?,
    var Title: String?,
    var Payable: Double?,
    var OverPay: Double?,
    var EmployeeName: String?,
    var CardNo: String?,
//    var SubId: Int,
    var VoucherList: List<Voucher>?,
    var CreateDate: String?
) : Parcelable {

}