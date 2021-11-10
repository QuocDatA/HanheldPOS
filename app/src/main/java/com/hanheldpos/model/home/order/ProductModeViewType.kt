package com.hanheldpos.model.home.order

enum class ProductModeViewType(val value: Int, var pos: Int? = 0) {
    Product(1),
    PrevButton(2),
    NextButton(3),
    Empty(4);
    companion object {
        fun fromInt(value: Int): ProductModeViewType? {
            ProductModeViewType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}