package com.hanheldpos.model.home.order.product

enum class ProductModeViewType(val value: Int, var pos: Int? = 0) {
    Product(1),
    PrevButton(2),
    NextButton(3),
    Empty(4);
}