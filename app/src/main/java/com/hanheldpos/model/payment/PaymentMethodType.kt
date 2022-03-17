package com.hanheldpos.model.cart.payment


enum class PaymentMethodType(val value: Int) {
    CASH(1),
    WALLET(4),
    OTHER_PAYMENT(5),
    GIFT_CARD(10);

    companion object {
        fun fromInt(value: Int): PaymentMethodType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}