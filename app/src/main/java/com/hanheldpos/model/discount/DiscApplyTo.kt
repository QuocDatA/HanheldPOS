package com.hanheldpos.model.discount

enum class DiscApplyTo(val value : Int) {
    UNKNOWN(0),
    ITEM(1),
    ORDER(2);
    companion object {
        fun fromInt(value: Int): DiscApplyTo {
            DiscApplyTo.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return UNKNOWN
        }
    }
}