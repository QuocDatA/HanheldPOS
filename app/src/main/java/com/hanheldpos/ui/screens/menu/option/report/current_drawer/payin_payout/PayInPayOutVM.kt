package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {
    val amountString = MutableLiveData<String>();
    var amount : Double? = null;
    var description = MutableLiveData<String>();

    val isValid = MediatorLiveData<Boolean>().apply {
        value = false;
    };

    init {

    }

    fun initLifeCycle(owner: LifecycleOwner) {
        isValid.addSource(amountString) {
            amount = it.replace(",","").toDouble();
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty();
            }
        }
        isValid.addSource(description) {
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty();
            }
        }
    }



    fun backPress() {
        uiCallback?.getBack()
    }
}