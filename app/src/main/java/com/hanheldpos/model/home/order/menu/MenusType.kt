package com.hanheldpos.model.home.order.menu

enum class MenusType(val value: Int) {
    SYSTEM_GROUP(0),
    Group(1),
    Category(2);
    companion object {
        fun fromInt(value: Int): MenusType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}