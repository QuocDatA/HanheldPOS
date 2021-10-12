package com.hanheldpos.ui.screens.product

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    val orderItemModel = MutableLiveData<OrderItemModel>();
    val numberQuantity = Transformations.map(orderItemModel) {
        return@map it.quantity;
    };
    val totalPriceLD = MutableLiveData(0.0);

    var maxQuantity = -1;


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        orderItemModel.observe(owner, {
            updateTotalPrice();
        })
    }

    fun onBack() {
        uiCallback?.onBack();
    }

    fun onQuantityAdded() {
        orderItemModel.value?.plusOrderQuantity(1);
        orderItemModel.notifyValueChange();
    }

    fun onQuantityRemoved() {
        orderItemModel.value?.minusOrderQuantity(1);
        orderItemModel.notifyValueChange();
    }

    fun updateTotalPrice() {
        totalPriceLD.value = (orderItemModel.value?.getPriceLineTotal() ?: 0.0);
    }

    //region Modifier
    fun onModifierQuantityChange(
        headerKey: String?,
        item: ModifierSelectedItemModel,
        isEdit: Boolean? = false
    ) {
        if (headerKey == null) return

        var modifierList = orderItemModel.value!!.extraDone?.selectedModifierGroup;
        if (modifierList == null) {
            modifierList = mutableListOf();
        }

        item.realItem?.let { real ->
            modifierList.find { it.realItem!!.modifierGuid == real.modifierGuid }.let {
                if (it != null) {
                    it.quantity = item.quantity;
                }
            }

        }
        orderItemModel.value!!.extraDone?.selectedModifierGroup = modifierList;
        orderItemModel.notifyValueChange()
    }

    //endregion

    //region Variant
    fun onVariantItemChange(item: GroupItem) {
        orderItemModel.value!!.extraDone?.selectedVariant = item.copy();
        orderItemModel.notifyValueChange();
    }

    //endregion
    fun onAddCart() {
        uiCallback?.onAddCart(orderItemModel.value!!);
    }
}