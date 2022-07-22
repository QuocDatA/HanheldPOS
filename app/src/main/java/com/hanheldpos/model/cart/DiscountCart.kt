package com.hanheldpos.model.cart

data class DiscountCart(
    val disOriginal : Any,
    val title : String,
    val amount : Double,
    val isDiscBuyXGetYEntire: Boolean? = false,
)