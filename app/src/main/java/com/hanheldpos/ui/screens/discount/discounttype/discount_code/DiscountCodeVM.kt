package com.hanheldpos.ui.screens.discount.discounttype.discount_code

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData<Boolean>(false);

    fun initData() {
        val listDiscountCode = DataHelper.discountResp?.Model?.filter { !it.DiscountAutomatic };
        if (listDiscountCode != null) uiCallback?.loadDataDiscountCode(listDiscountCode as List<DiscountItem>);
    }
}