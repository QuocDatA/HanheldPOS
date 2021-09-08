package com.hanheldpos.ui.screens.home.table.input

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TableInputVM : BaseUiViewModel<TableInputUV>() {
    fun onComplete(){
        uiCallback?.onComplete();
    }
    fun onCancel(){
        uiCallback?.onCancel();
    }
}