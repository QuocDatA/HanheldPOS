package com.hanheldpos.model.payment.method

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp

abstract class BaseCardPayment(
    paymentMethod: PaymentMethodResp,
    listener: PaymentMethodCallback,
) : BasePayment(
    paymentMethod,
    listener,
) {
    open fun onValidPayment(balance: Double, cardCode : String?, balanceCart: Double?, orderId: String, customerId: String?) {
        listener.onShowPaymentInputAmount(this, balance, orderId, customerId,cardCode, balanceCart)
    }
}