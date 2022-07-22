package com.hanheldpos.model.menu.settings

enum class GeneralNotificationType(var value: Int? = null) {
    DEFAULT(30), TIME_1M(60), TIME_5M(5 * 60), FOREVER(0);

    companion object {
        fun fromInt(value: Int): GeneralNotificationType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}