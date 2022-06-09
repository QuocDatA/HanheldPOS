package com.hanheldpos.model.payment

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.Voucher
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.utils.DateTimeUtils
import kotlinx.parcelize.Parcelize
import java.util.*

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
    constructor(
        paymentMethod: PaymentMethodResp,
        payable: Double,
        overPay: Double,
        cardNo: String? = null,
        voucherList: List<Voucher>? = null
    ) : this(
        _id = paymentMethod._id,
        ApplyToId = paymentMethod.ApplyToId,
        Payable = payable,
        OverPay = overPay,
        Title = DataHelper.paymentMethodsLocalStorage?.find { it._id == paymentMethod._id }?.Title,
        PaymentTypeId = paymentMethod.PaymentMethodType,
        CardNo = cardNo,
        EmployeeName = UserHelper.curEmployee?.FullName,
        CreateDate = DateTimeUtils.dateToString(
            Date(),
            DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS
        ),
        VoucherList = voucherList
    )
}