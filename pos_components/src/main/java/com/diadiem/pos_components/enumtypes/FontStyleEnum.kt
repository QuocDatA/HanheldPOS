package com.diadiem.pos_components.enumtypes

enum class FontStyleEnum(val value: Int) {
    NORMAL(value = 1),
    ITALIC(value = 2),
    BOLD(value = 3),
    SEMI_BOLD(value = 4);


    companion object{
        fun fromInt(value: Int): FontStyleEnum?{
            return values().find {
                it.value == value
            }
        }
    }
}