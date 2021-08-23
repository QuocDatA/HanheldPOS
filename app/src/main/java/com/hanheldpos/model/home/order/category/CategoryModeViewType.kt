package com.hanheldpos.model.home.order.category

enum class CategoryModeViewType(val value: Int,var pos : Int? = 0) {
    Category(1),
    DirectionButton(2,0),
    Empty(3);
}