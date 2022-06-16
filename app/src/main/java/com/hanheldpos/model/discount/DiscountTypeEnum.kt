package com.hanheldpos.model.discount

import com.hanheldpos.model.product.combo.ItemActionType

enum class DiscountTypeEnum(val value: Int) {
    PERCENT(1),
    AMOUNT (2),
    FREE_SHIP (3),
    BUYX_GETY (4),
    PRICE_OVERRIDE (5);
    companion object {
        fun fromInt(value: Int): DiscountTypeEnum? {
            DiscountTypeEnum.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}