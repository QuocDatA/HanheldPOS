package com.hanheldpos.model.product.buy_x_get_y


enum class CustomerDiscApplyTo(val value: Int) {
    UNKNOWN(0),
    ENTIRE_ORDER(1),
    PRODUCT(2),
    GROUP(3),
    CATEGORY(4);
    companion object {
        fun fromInt(value: Int): CustomerDiscApplyTo {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return UNKNOWN
        }
    }
}