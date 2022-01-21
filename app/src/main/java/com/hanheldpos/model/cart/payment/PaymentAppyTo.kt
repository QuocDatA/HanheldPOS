package com.hanheldpos.model.cart.payment

enum class PaymentAppyTo(val value: Int) {
    CASH(1),
    INPUT_CARD_NUMBER (3),
    JCB_CARD (30),
    API (5),
    CASH_VOUCHER (6),
    SODEXO (8),
    GRAB (30),
    ZALO_PAY (30),
    GIFT_CARD (10)
}