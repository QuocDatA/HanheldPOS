package com.hanheldpos.ui.screens.menu.discount

import androidx.lifecycle.ViewModel
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class MenuDiscountVM : BaseUiViewModel<MenuDiscountUV>() {
    fun onFragmentBackPress() {
        uiCallback?.onFragmentBackPressed()
    }
    fun initListDiscountCode() {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        val sortList = listDiscountCode?.sortedBy { it.DiscountName }
        uiCallback?.loadDiscountCode(sortList ?: listOf())
    }
}