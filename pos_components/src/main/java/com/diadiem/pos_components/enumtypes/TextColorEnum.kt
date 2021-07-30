package com.diadiem.pos_components.enumtypes

enum class TextColorEnum(val value: Int) {
    Color0(value = 0),
    Color1(value = 1),
    Color2(value = 2),
    Color3(value = 3),
    Color4(value = 4),
    Color5(value = 5),
    Color6(value = 6),
    Color7(value = 7),
    Color8(value = 8),
    Color9(value = 9),
    Color10(value = 10),
    Color11(value = 11),
    Color12(value = 12),
    Color13(value = 13),
    Color14(value = 14),
    Color15(value = 15);

    companion object{
        fun fromInt(value: Int): TextColorEnum?{
            return values().find {
                it.value == value
            }
        }
    }
}