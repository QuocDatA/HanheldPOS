package com.hanheldpos.model.discount

enum class DiscountTypeFor(val value : Int) {
    AMOUNT(0),
    PERCENTAGE(1),
    DISCOUNT_CODE(2),
    AUTOMATIC(3),
    COMP(4)
}