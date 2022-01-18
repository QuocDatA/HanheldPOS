package com.hanheldpos.model.fee


enum class ChooseProductApplyTo(val value : Int) {
    PRO_GET(2),
    DEFAULT(1);
    companion object {
        fun fromInt(value: Int): ChooseProductApplyTo? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}