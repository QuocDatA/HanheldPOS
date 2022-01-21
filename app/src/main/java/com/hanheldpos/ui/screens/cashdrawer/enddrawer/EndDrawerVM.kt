package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class EndDrawerVM : BaseUiViewModel<EndDrawerUV>() {

    val amountString  = MutableLiveData<String>();
    val amount :Double? = null;
    val description = MutableLiveData<String>();

    fun backPress(){
        uiCallback?.backPress();
    }
}