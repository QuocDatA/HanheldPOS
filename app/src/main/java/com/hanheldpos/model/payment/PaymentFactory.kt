package com.hanheldpos.model.payment

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.payment.method.*

object PaymentFactory {
    fun getPaymentMethod(
        payment: PaymentMethodResp,
        callback: BasePayment.PaymentMethodCallback
    ): BasePayment {
        when (PaymentMethodType.fromInt(payment.PaymentMethodType)) {
            PaymentMethodType.CASH -> return CashPayment(payment, callback)
            PaymentMethodType.WALLET -> return WalletPayment(payment, callback)
            PaymentMethodType.GIFT_CARD -> return GiftCartPayment(payment, callback)
            null -> throw Exception("Payment $payment is not identify")
            else -> {
                return when (PaymentApplyTo.fromInt(payment.ApplyToId)) {
                    PaymentApplyTo.API -> MoMoPayment(payment, callback)
                    PaymentApplyTo.CASH_VOUCHER -> CashVoucherPayment(payment, callback)
                    PaymentApplyTo.SODEXO -> SodexoPayment(payment, callback)
                    null -> throw Exception("Payment $payment is not identify")
                    else -> {
                        OtherPayment(payment, callback)
                    }
                }
            }
        }
    }
}