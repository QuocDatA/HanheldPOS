package com.hanheldpos.ui.screens.discount.discounttype.amount

import android.service.autofill.Transformation
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import javax.xml.transform.Transformer

class DiscountAmountVM : BaseUiViewModel<DiscountAmountUV>() {
    val amount = MutableLiveData<String>(0.toString());
    val title = MutableLiveData<String>("");
    var amountValue : Double = 0.0;

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        amount.observe(owner,{
            amountValue = it.replace(",","").toDouble();
        })
    }

}