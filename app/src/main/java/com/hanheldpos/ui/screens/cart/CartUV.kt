package com.hanheldpos.ui.screens.cart

import com.hanheldpos.data.api.pojo.loyalty.LoyaltyResp
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.BaseUserView

interface CartUV : BaseUserView {
    fun getBack()
    fun deleteCart()
    fun onOpenDiscount()
    fun openSelectPayment(alreadyBill : Boolean,payable : Double, paymentList : List<PaymentOrder>?)
    fun onOpenAddCustomer()
    fun onBillSuccess(orderModel: OrderModel)
    fun onFinishOrder(isSuccess : Boolean, loyaltyResp: LoyaltyResp? = null)
    fun onShowCustomerDetail()
}