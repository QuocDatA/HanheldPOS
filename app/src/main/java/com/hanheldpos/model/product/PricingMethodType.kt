package com.hanheldpos.model.product

enum class PricingMethodType(val value : Int) {
    BasePrice(1),
    GroupPrice(2);

    companion object {
        fun fromInt(value: Int): PricingMethodType? {
            PricingMethodType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}