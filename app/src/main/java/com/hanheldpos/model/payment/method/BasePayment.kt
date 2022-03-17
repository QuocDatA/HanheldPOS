package com.hanheldpos.model.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.payment.PaymentOrder

abstract class BasePayment(
    public val paymentMethod: PaymentMethodResp,
    protected val listener: PaymentMethodCallback,
) {
    abstract fun startPayment(balance: Double, orderId: String, customerId: String?)

    open fun getPayable(overPay: Double, balance: Double): Double {
        return if (balance > overPay) overPay else balance
    }

    interface PaymentMethodCallback {
        fun onShowPaymentInputAmount(base : BasePayment, balance: Double,orderId: String, customerId: String?, balanceCart : Double? = null )
        fun onShowPaymentInputCartNumber(base : BasePayment, balance: Double, orderId: String, customerId: String?)
        fun onShowCashVoucherList()
    }
}