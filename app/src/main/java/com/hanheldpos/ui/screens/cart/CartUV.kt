package com.hanheldpos.ui.screens.cart

import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.BaseUserView

interface CartUV : BaseUserView {
    fun getBack()
    fun deleteCart()
    fun onOpenDiscount()
    fun openSelectPayment(alreadyBill : Boolean,payable : Double, paymentList : List<PaymentOrder>?)
    fun onOpenAddCustomer()
    fun onBillSuccess()
    fun onFinishOrder(isSuccess : Boolean)
    fun onShowCustomerDetail()
}