package com.hanheldpos.ui.screens.cart

import com.hanheldpos.ui.base.BaseUserView

interface CartUV : BaseUserView {
    fun getBack();
    fun deleteCart();
    fun onOpenDiscount();
    fun openSelectPayment(alreadyBill : Boolean,payable : Double);
    fun onOpenAddCustomer();
    fun onBillSuccess();
    fun onPayment(isSuccess : Boolean)
    fun onShowCustomerDetail();
}