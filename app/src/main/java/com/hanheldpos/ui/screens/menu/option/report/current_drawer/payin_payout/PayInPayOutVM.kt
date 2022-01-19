package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {
    val amountString = MutableLiveData<String>();
    var amount : Double? = null;

    var description = MutableLiveData<String>();

    fun initLifeCycle(owner: LifecycleOwner) {
        amountString.observe(owner,{
            amount = it.replace(",","").toDouble();
        });
    }

    fun isValid() : Boolean {
        amount?:return false;
        description.value?:return false;
        return amount!! > 0.0 && !description.value?.trim().isNullOrEmpty();
    }

    fun backPress() {
        uiCallback?.getBack()
    }
}