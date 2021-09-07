package com.hanheldpos.ui.screens.home.order

import com.hanheldpos.model.product.ProductCompleteModel

object OrderHelper {
    val cart: MutableList<ProductCompleteModel> = mutableListOf()

    fun totalQuantity() : Int {
        return cart.sumOf {
            it.quantity
        }
    }
}

