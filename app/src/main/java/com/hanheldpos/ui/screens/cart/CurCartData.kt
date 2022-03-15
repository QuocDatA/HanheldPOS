package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.model.cart.CartModel

object CurCartData {
    var cartModel: CartModel? = null
    var tableFocus : FloorTable? = null
}