package com.hanheldpos.ui.screens.product

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.getPriceByExtra
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    val extraDoneModel = MutableLiveData<ExtraDoneModel>();
    val numberQuantity = Transformations.map(extraDoneModel) {
        return@map it.quantity;
    };
    val totalPriceLD = MutableLiveData(0.0);
    //
    var maxQuantity = -1;


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        extraDoneModel.observe(owner,{
            updateTotalPrice();
        })
    }

    fun onBack() {
        uiCallback?.onBack();
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity){
            extraDoneModel.value?.quantity = numberQuantity.value?.plus(1)!!;
            extraDoneModel.notifyValueChange();
        }

    }
    fun onRemoveQuantity(){
        if (numberQuantity.value!! > 0){
            extraDoneModel.value?.quantity = numberQuantity.value?.minus(1)!!;
            extraDoneModel.notifyValueChange();
        }
    }

    fun updateTotalPrice(){
        totalPriceLD.value =(extraDoneModel.value?.getPriceByExtra() ?: 0.0);
    }

    //region Modifier
    fun onModifierQuantityChange(headerKey: String?, item: ModifierSelectedItemModel) {
        if (headerKey == null) return

        var modifierMap = extraDoneModel.value?.selectedModifierGroup
        if (modifierMap == null) {
            modifierMap = mutableMapOf()
        }

        if (!modifierMap.containsKey(headerKey)) {
            modifierMap[headerKey] = LinkedHashSet()
        }

        item.realItem?.let {
            if (item.quantity == 0)
                modifierMap[headerKey]?.remove(item)
            else
                modifierMap[headerKey]?.add(item)



        }
        extraDoneModel.value?.selectedModifierGroup = modifierMap
        extraDoneModel.notifyValueChange()
    }

    //endregion

    //region Variant
    fun onVariantItemChange(item : GroupItem){
        extraDoneModel.value?.selectedVariant = item;
        extraDoneModel.notifyValueChange();
    }
    //endregion
    fun onAddCart(){
        uiCallback?.onAddCart(extraDoneModel.value!!);
    }
}