package com.hanheldpos.model.home.order.menu

enum class MenuModeViewType(val value: Int) {
    Menu(1),
    DirectionDisableUpDown(2),
    DirectionEnableDown(3),
    DirectionEnableUp(4),
    DirectionEnableUpDown(5),
    Empty(6);
    /*
            * 0 : Disable All
            * 1 : Enable Next
            * 2 : Enable Previous
            * 3 : Enable All
            * */
}