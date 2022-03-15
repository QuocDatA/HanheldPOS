package com.hanheldpos.model.cart.payment

enum class PaymentMethodType(val value: Int) {
    CASH(1),
    WALLET(4),
    OTHER_PAYMENT(5),
    CASH_VOUCHER(6),
    GIFT_CARD(10)
}