package com.hanheldpos.model.discount

enum class DiscountEntireType(val value : Int) {
    NONE(4),
    FREE(3),
    PERCENT(1),
    AMOUNT(2),
    SPECIFIC(5);
    companion object {
        fun fromInt(value: Int): DiscountEntireType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}