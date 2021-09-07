package com.hanheldpos.ui.screens.product

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import com.hanheldpos.data.api.pojo.product.getPriceByExtra
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    val productCompletelLD = MutableLiveData<ProductCompleteModel>();
    val productDetailLD = MutableLiveData<ProductDetailResp?>();
    val numberQuantity = Transformations.map(productCompletelLD) {
        return@map it.quantity;
    };
    val totalPriceLD = MutableLiveData(0.0);
    //
    var maxQuantity = -1;


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        productCompletelLD.observe(owner,{
            updateTotalPrice();
        })
        productDetailLD.observe(owner,{
            updateTotalPrice();
        })
    }

    fun onBack() {
        uiCallback?.onBack();
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity){
            productCompletelLD.value?.quantity = numberQuantity.value?.plus(1)!!;
            productCompletelLD.notifyValueChange();
        }

    }
    fun onRemoveQuantity(){
        if (numberQuantity.value!! > 0){
            productCompletelLD.value?.quantity = numberQuantity.value?.minus(1)!!;
            productCompletelLD.notifyValueChange();
        }
    }

    fun updateTotalPrice(){
        totalPriceLD.value =(productCompletelLD.value?.getPriceTotal() ?: 0.0);
    }

    fun onModifierQuantityChange(headerKey: String?, item: ModifierSelectedItemModel) {
        if (headerKey == null) return

        var modifierMap = productDetailLD.value?.selectedModifierGroup
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
        productDetailLD.value?.selectedModifierGroup = modifierMap
        productDetailLD.notifyValueChange()
    }

    fun onAddCart(){
        uiCallback?.onAddCart(productCompletelLD.value!!);
    }
}