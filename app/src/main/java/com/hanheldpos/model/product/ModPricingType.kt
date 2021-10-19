package com.hanheldpos.model.product


enum class ModPricingType(val value : Int) {
    USED_DEFAULT_PRICE(1),
    NONE(2),
    DISCOUNT_AMOUNT(3),
    DISCOUNT_PERCENT(4),
    FIX_AMOUNT(5),
    NOT_FOUND(-1);

    companion object {
        fun fromInt(value: Int): ModPricingType? {
            ModPricingType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }

}