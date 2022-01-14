package com.hanheldpos.ui.screens.product

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    val isValidDiscount = MutableLiveData<Boolean>(false);
    var typeDiscountSelect : DiscountTypeFor?= null;

    val listVariantGroups : MutableList<VariantsGroup> =mutableListOf();
    val listModifierGroups :  MutableList<GroupExtra> = mutableListOf();

    val regularInCart = MutableLiveData<Regular>();
    val actionType = MutableLiveData<ItemActionType>();


    val numberQuantity = Transformations.map(regularInCart) {
        return@map it.quantity;
    };
    val totalPriceLD = Transformations.map(regularInCart) {
        return@map regularInCart.value?.total() ?: 0.0
    }

    var maxQuantity = -1;

    var minQuantity: LiveData<Int> = Transformations.map(actionType) {
        return@map when (actionType.value) {
            ItemActionType.Modify -> 0;
            ItemActionType.Add -> 1;
            else -> 1;
        };
    };

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun onQuantityAdded() {
        if (numberQuantity.value!! < maxQuantity)
            regularInCart.value?.plusOrderQuantity(1);
        regularInCart.notifyValueChange();
    }

    fun onQuantityRemoved() {
        if (minQuantity.value == numberQuantity.value) return;
        regularInCart.value?.minusOrderQuantity(1);
        regularInCart.notifyValueChange();
    }

    fun onAddCart() {
        uiCallback?.onAddCart(regularInCart.value!!);
    }

    fun onGetBack(){
        uiCallback?.getBack();
    }


}