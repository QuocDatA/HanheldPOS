package com.hanheldpos.model.cart.fee

import com.fasterxml.jackson.annotation.JsonValue

enum class FeeType (@JsonValue val value:Int ){
    ServiceFee(1),
    SurchargeFee(2),
    TaxFee(3),
    ShippingFee(4);
    companion object {
        fun fromInt(value: Int): FeeType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}