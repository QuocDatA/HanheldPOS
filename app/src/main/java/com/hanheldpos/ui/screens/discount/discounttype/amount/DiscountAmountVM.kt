package com.hanheldpos.ui.screens.discount.discounttype.amount

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountAmountVM : BaseUiViewModel<DiscountAmountUV>() {
    val amount = MutableLiveData<String>(0.toString());
    val title = MutableLiveData<String>("");

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        amount.observe(owner,{

        })
    }

}