package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CartVM : BaseUiViewModel<CartUV>() {

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun backPress() {
        uiCallback?.getBack();
    }

    fun deleteCart() {
        uiCallback?.deleteCart();
    }

    fun openDiscount() {
        uiCallback?.onOpenDiscount();
    }

    fun openSelectPayment() {
        uiCallback?.openSelectPayment();
    }

    fun onOpenAddCustomer() {
        uiCallback?.onOpenAddCustomer();
    }

    fun processDataDiscount(cart: CartModel): List<DiscountCart> {
        val list = mutableListOf<DiscountCart>();
        cart.compReason?.let {
            list.add(DiscountCart(it, it.title!!, cart.totalComp(cart.totalTemp())));
        }
        return list;
    }

}