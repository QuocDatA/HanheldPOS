package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData<Boolean>(false);

    fun initData() {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        if (listDiscountCode != null) uiCallback?.loadDataDiscountCode(listDiscountCode as List<DiscountResp>);
    }

    fun searchDiscountCode(keyword: String) {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        val searchList =  listDiscountCode?.filter { it.DiscountCode.lowercase().contains(keyword.lowercase()) }
        uiCallback?.loadDataDiscountCode(searchList as List<DiscountResp>);
    }
}