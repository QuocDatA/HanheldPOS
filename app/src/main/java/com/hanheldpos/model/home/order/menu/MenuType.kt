package com.hanheldpos.model.home.order.menu

enum class MenuType(val value: Int) {
    Category(1),
    Group(2);
    companion object {
        fun fromInt(value: Int): MenuType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}