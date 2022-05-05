package com.hanheldpos.model.discount

import com.hanheldpos.model.fee.ChooseProductApplyTo

enum class DiscountTypeFor(val value : Int) {
    AMOUNT(0),
    PERCENTAGE(1),
    DISCOUNT_CODE(2),
    AUTOMATIC(3),
    COMP(4);
    companion object {
        fun fromInt(value: Int): DiscountTypeFor? {
            DiscountTypeFor.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}