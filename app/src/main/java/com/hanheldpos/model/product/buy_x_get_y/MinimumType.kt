package com.hanheldpos.model.product.buy_x_get_y

enum class MinimumType(val value: Int) {
    UNKNOWN( 0),
    QUANTITY (1),
    AMOUNT ( 2);
    companion object {
        fun fromInt(value: Int): MinimumType {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return UNKNOWN
        }
    }
}