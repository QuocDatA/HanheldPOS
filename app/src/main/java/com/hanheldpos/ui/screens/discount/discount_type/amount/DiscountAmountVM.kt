package com.hanheldpos.ui.screens.discount.discount_type.amount

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountAmountVM : BaseUiViewModel<DiscountAmountUV>() {
    val amount = MutableLiveData<String>(0.toString());
    val title = MutableLiveData<String>("");
    var amountValue : Double = 0.0;

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        amount.observe(owner,{
            val result = it.replace(",","");
            if(result.isNotEmpty())
            amountValue = result.toDouble();
        })
    }

}