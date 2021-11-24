package com.hanheldpos.ui.screens.discount.discounttype.percentage

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountPercentageVM : BaseUiViewModel<DiscountPercentageUV>() {
    val percent = MutableLiveData<String>(0.toString());

    val title = MutableLiveData<String>();
    var amountValue : Double = 0.0
    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        percent.observe(owner,{
            amountValue = it.toDouble();
        })
    }
}