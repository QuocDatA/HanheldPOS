package com.hanheldpos.ui.screens.home.table.customer_input

import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TableInputVM : BaseUiViewModel<TableInputUV>() {

    var numberCustomer : Long = 0

    fun onComplete(){
        uiCallback?.onComplete();
    }
    fun onCancel(){
        uiCallback?.onCancel();
    }
}