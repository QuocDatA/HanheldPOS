package com.hanheldpos.model.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp

abstract class BasePayment(
    val paymentMethod: PaymentMethodResp,
    protected val listener: PaymentMethodCallback,
) {
    abstract fun startPayment(balance: Double, orderId: String, customerId: String?)

    open fun getPayable(overPay: Double, balance: Double): Double {
        return if (balance > overPay) overPay else balance
    }

    interface PaymentMethodCallback {
        fun onShowPaymentInputAmount(
            base: BasePayment,
            balance: Double,
            orderId: String,
            customerId: String?,
            cardCode : String? = null,
            balanceCart: Double? = null
        )

        fun onShowPaymentInputCartNumber(
            base: BaseCardPayment,
            balance: Double,
            orderId: String,
            customerId: String?
        )

        fun onShowCashVoucherList(base: BasePayment)
    }
}