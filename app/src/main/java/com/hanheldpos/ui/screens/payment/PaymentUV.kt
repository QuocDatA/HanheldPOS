package com.hanheldpos.ui.screens.payment

import com.hanheldpos.model.payment.method.BasePayment
import com.hanheldpos.ui.base.BaseUserView

interface PaymentUV : BaseUserView {
    fun getBack()
    fun openPaymentDetail()
    fun onValidCardNumber(payment : BasePayment,balanceCart : Double?)
    fun onFragmentBackPressed()
}