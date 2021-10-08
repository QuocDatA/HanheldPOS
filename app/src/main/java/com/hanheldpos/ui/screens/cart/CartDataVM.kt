package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.settings.ListDiningOptionsItem
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class CartDataVM : BaseViewModel() {

    val cartModelLD: MutableLiveData<CartModel> = MutableLiveData(CartModel());

    val diningOptionLD: LiveData<ListDiningOptionsItem> = Transformations.map(cartModelLD) {
        return@map it?.diningOption ?: DataHelper.getDefaultDiningOptionItem()
    }

    val numberOfCustomer : LiveData<Int> = Transformations.map(cartModelLD) {
        return@map it?.customerQuantity;
    }

    fun addToCart(item : OrderItemModel){
        this.cartModelLD.value!!.listOrderItem.add(item);
        this.cartModelLD.notifyValueChange();
    }
}