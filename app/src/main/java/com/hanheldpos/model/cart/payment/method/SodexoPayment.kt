package com.hanheldpos.model.cart.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp

class SodexoPayment(paymentMethod: PaymentMethodResp,
                    listener: PaymentMethodCallback,
) : BasePayment(paymentMethod, listener) {
    override fun startPayment(balance: Double, orderId: String, customerId: String?) {
        listener.onShowPaymentInput(this,balance, orderId, customerId)
    }
}