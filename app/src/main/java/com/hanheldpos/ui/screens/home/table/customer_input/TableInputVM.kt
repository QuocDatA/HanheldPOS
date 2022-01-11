package com.hanheldpos.ui.screens.home.table.customer_input

import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TableInputVM : BaseUiViewModel<TableInputUV>() {
    fun onComplete(){
        uiCallback?.onComplete();
    }
    fun onCancel(){
        uiCallback?.onCancel();
    }
    fun onSwitch(keyBoardType : KeyBoardType) {
        uiCallback?.onSwitch(keyBoardType);
    }
}