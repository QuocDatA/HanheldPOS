package com.hanheldpos.ui.screens.home.table.customer_input

import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.BaseUserView

interface TableInputUV  : BaseUserView{
    fun onCancel();
    fun onComplete();
    fun onSwitch(keyBoardType: KeyBoardType);
}