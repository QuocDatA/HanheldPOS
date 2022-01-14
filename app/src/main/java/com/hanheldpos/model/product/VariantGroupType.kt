package com.hanheldpos.model.product

enum class VariantGroupType(val value : Int)  {
    ButtonStyle(1),
    RadioStyle(2);

    companion object {
        fun fromInt(value: Int): VariantGroupType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}