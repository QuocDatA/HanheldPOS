package com.diadiem.pos_components.enumtypes

enum class TextHeaderEnum(val value: Int) {
    H1(value = 1),
    H2(value = 2),
    H3(value = 3),
    H4(value = 4),
    H5(value = 5),
    H6(value = 6),
    H7(value = 7);

    companion object{
        fun fromInt(value: Int): TextHeaderEnum?{
            return values().find {
                it.value == value
            }
        }
    }
}