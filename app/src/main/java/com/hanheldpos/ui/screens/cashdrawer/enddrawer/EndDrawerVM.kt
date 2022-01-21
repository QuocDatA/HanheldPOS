package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class EndDrawerVM : BaseUiViewModel<EndDrawerUV>() {

    val amountString = MutableLiveData<String>();
    var amount: Double? = null;
    val description = MutableLiveData<String>();

    val isValid = MediatorLiveData<Boolean>().apply {
        value = false;
    };

    init {
        isValid.addSource(amountString) {
            amount = if (it.isNullOrEmpty()) {
                null;
            } else
                it.replace(",", "").toDouble();
            isValid.value = if (amount == null) {
                false
            } else {
                amount!! > 0.0
            }
        }
    }

    fun backPress() {
        uiCallback?.getBack()
    }
}