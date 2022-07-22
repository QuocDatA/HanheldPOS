package com.hanheldpos.model.cashdrawer

enum class DrawerStatus(val value: Int) {
    NOT_FOUND(0),
    DRAWER_STARTED(1),
    DRAWER_ENDED(3);

    companion object {
        fun fromInt(value: Int): DrawerStatus? {
            DrawerStatus.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}