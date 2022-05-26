package com.hanheldpos.model.product


enum class ProductType(val value : Int) {
    UNKNOWN(0),
    REGULAR(1),
    BUNDLE(2),
    GROUP_SKU(3),
    BUYX_GETY_DISC(4),
    COMBO_DISC(5);
    companion object {
        fun fromInt(value: Int): ProductType? {
            ProductType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}