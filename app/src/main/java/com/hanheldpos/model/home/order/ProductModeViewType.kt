package com.hanheldpos.model.home.order

enum class ProductModelViewType(val value: Int) {
    Product(1),
    PrevButtonEnable(2),
    PrevButtonDisable(3),
    NextButtonEnable(4),
    NextButtonDisable(5),
    Empty(6),
    Combo(7);
    companion object {
        fun fromInt(value: Int): ProductModelViewType? {
            ProductModelViewType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}