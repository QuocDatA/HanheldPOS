package com.hanheldpos.model.cart.order

enum class OrderItemType(val value: Int) {
    BUNDLE(0),
    Product(1),
    BuyXGetY(2),
    Combo(3),
}