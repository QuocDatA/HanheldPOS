package com.hanheldpos.model.discount

import com.hanheldpos.model.fee.ChooseProductApplyTo

enum class DiscountTypeTo(val value : Int) {
    NOT_FOUND(0),
    ITEM(1),
    ORDER(2);
    companion object {
        fun fromInt(value: Int): DiscountTypeTo? {
            DiscountTypeTo.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}