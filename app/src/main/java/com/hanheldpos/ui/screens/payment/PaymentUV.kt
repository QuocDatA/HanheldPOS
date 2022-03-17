package com.hanheldpos.ui.screens.payment

import com.hanheldpos.model.payment.method.BaseCardPayment
import com.hanheldpos.model.payment.method.BasePayment
import com.hanheldpos.ui.base.BaseUserView

interface PaymentUV : BaseUserView {
    fun getBack()
    fun openPaymentDetail()
    fun onValidCardNumber(payment : BaseCardPayment, cardCode : String?, balanceCart : Double?)
    fun paymentChosenSuccess(payment: BasePayment, amount: Double)
    fun onFragmentBackPressed()
}