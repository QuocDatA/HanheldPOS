package com.hanheldpos.model.cart.payment

data class PaymentOrder(
    var PaymentTypeId: Int,
    var ApplyToId: Int,
    var SubId: Int,
    var Title: String,
    var Payable: Double,
    var OverPay: Double,
    var EmployeeName: String,
    var CardNo: String,
    // var VoucherList:
    var CreateDate: String
) {

}