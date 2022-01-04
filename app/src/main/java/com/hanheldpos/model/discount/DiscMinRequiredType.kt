package com.hanheldpos.model.discount

enum class DiscMinRequiredType(val value: Int) {
    NONE(1),
    AMOUNT(2),
    QUANTITY(3);

    companion object {
        fun fromInt(value: Int): DiscMinRequiredType? {
            DiscMinRequiredType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}