package com.hanheldpos.model.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp

class GiftCartPayment(
    paymentMethod: PaymentMethodResp,
    listener: PaymentMethodCallback,
) : BasePayment(paymentMethod, listener) {
    override fun startPayment(balance: Double, orderId: String, customerId: String?) {
        listener.onShowPaymentInputCartNumber(this, balance, orderId, customerId)
    }

    fun onValidPayment(balance: Double, balanceCart: Double?, orderId: String, customerId: String?) {
        listener.onShowPaymentInputAmount(this, balance, orderId, customerId, balanceCart)
    }
}