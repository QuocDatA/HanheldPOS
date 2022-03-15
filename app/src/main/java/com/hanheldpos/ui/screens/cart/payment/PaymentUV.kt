package com.hanheldpos.ui.screens.cart.payment

import com.hanheldpos.ui.base.BaseUserView

interface PaymentUV : BaseUserView {
    fun getBack()
    fun getPayment()
    fun openPaymentDetail()
    fun onFragmentBackPressed()
}