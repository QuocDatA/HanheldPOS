package com.hanheldpos.model.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp

class WalletPayment(paymentMethod: PaymentMethodResp,
                    listener: PaymentMethodCallback,
) : BaseCardPayment(paymentMethod, listener) {
    override fun startPayment(balance: Double, orderId: String, customerId: String?) {
        listener.onShowPaymentInputCartNumber(this,balance, orderId, customerId)
    }
}