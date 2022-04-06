package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData<Boolean>(false);
    fun initData() {
       searchDiscountCode()
    }

    fun searchDiscountCode(keyword: String = "") {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        val searchList =  listDiscountCode?.sortedBy { it.DiscountName }?.filter { it.DiscountCode.lowercase().contains(keyword.lowercase()) }
        searchList?.let {
            uiCallback?.loadDataDiscountCode(searchList)
        }
    }


}