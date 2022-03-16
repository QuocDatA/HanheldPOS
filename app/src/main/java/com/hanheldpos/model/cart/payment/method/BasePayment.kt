package com.hanheldpos.model.cart.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.cart.payment.PaymentOrder

abstract class BasePayment(
    public val paymentMethod: PaymentMethodResp,
    protected val listener: PaymentMethodCallback,
) {
    abstract fun startPayment(balance: Double, orderId: String, customerId: String?)

    open fun getPayable(overPay: Double, balance: Double): Double {
        return if (balance > overPay) overPay else balance
    }

    interface PaymentMethodCallback {
        fun onShowPaymentInput(base : BasePayment, balance: Double, orderId: String, customerId: String?)
        fun onShowCashVoucherList()
    }
}