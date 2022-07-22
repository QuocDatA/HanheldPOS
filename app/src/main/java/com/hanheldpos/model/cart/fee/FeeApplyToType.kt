package com.hanheldpos.model.cart.fee

import com.fasterxml.jackson.annotation.JsonValue

enum class FeeApplyToType(@JsonValue val value: Int) {
    NotIncluded(1),
    Included(2),
    Order(3);
    companion object {
        fun fromInt(value: Int): FeeApplyToType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}