package com.hanheldpos.model.discount

enum class DiscountApplyTo(val value :Int) {
    UNKNOWN ( 0),
    ITEM (1),
    ORDER ( 2)
}