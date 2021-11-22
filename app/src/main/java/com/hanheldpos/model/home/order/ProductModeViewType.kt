package com.hanheldpos.model.home.order

enum class ProductModeViewType(val value: Int) {
    Product(1),
    PrevButtonEnable(2),
    PrevButtonDisable(3),
    NextButtonEnable(4),
    NextButtonDisable(5),
    Empty(6);
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