package com.hanheldpos.model.order

enum class OrderType(val value : Int) {
    REGULAR(0),
    BUYXGETY_GET(1),
    BUYXGETY_BUY(2),
    COMBO(3),
    BUNDLE(4);
}