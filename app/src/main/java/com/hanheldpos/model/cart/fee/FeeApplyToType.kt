package com.hanheldpos.model.cart.fee

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.hanheldpos.model.home.order.ProductModeViewType

enum class FeeApplyToType(@JsonValue val value: Int) {
    NotIncluded(1),
    Included(2),
    Order(3);
    companion object {
        fun fromInt(value: Int): FeeApplyToType? {
            FeeApplyToType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}