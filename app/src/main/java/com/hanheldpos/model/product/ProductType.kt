package com.hanheldpos.model.product

enum class ProductType(value : Int) {
    NOT_FOUND(0),
    REGULAR(1),
    BUNDLE(2),
    GROUP_SKU(3),
    BUYX_GETY_DISC(4),
    COMBO_DISC(5)
}