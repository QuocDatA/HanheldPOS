package com.hanheldpos.ui.screens.product

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    val productCompletelLD = MutableLiveData<ProductCompleteModel>();
    val productDetailLD = MutableLiveData<ProductDetailResp?>();
    val numberQuantity = MutableLiveData(1);
    val totalPriceLD = MutableLiveData(0.0);
    //
    var maxQuantity = -1;


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

    }

    fun onBack() {
        uiCallback?.onBack();
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity)
            numberQuantity.value = numberQuantity.value?.plus(1);
    }
    fun onRemoveQuantity(){
        if (numberQuantity.value!! > 0)
            numberQuantity.value = numberQuantity.value?.minus(1);
    }
}