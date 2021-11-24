package com.hanheldpos.model.discount

enum class DiscountTypeFor(val value : Int) {
    AMOUNT(0),
    PERCENTAGE(1),
    DISCOUNT_CODE(2),
    COMP(3)
}