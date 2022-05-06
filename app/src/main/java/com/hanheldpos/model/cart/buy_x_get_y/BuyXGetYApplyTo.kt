package com.hanheldpos.model.cart.buy_x_get_y


enum class BuyXGetYApplyTo(val value: Int) {
    UNKNOWN(0),
    ENTIRE_ORDER(1),
    PRODUCT(2),
    GROUP(3),
    CATEGORY(4);
    companion object {
        fun fromInt(value: Int): BuyXGetYApplyTo {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return UNKNOWN
        }
    }
}