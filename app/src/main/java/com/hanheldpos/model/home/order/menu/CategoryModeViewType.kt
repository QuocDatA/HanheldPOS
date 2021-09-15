package com.hanheldpos.model.home.order.menu

enum class MenuModeViewType(val value: Int,var pos : Int? = 0) {
    Menu(1),
    DirectionButton(2,0),
    Empty(3);
}