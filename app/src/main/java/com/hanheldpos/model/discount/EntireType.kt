package com.hanheldpos.model.discount

enum class EntireType(val value: Int) {
    BUY(1),
    GET(2);

    companion object {
        fun fromInt(value: Int): EntireType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}