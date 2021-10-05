package com.hanheldpos.model.product

enum class ModPricingType(value : Int) {
    USED_DEFAULT_PRICE(1),
    NONE(2),
    DISCOUNT_AMOUNT(3),
    DISCOUNT_PERCENT(4),
    FIX_AMOUNT(5),
    NOT_FOUND(-1);
}