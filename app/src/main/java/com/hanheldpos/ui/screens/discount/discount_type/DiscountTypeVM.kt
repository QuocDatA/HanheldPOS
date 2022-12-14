package com.hanheldpos.ui.screens.discount.discount_type

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountTypeVM : BaseUiViewModel<DiscountTypeUV>() {
    var typeDiscountSelect = MutableLiveData<DiscountTypeFor>()
    var isExistDiscountToClear = MutableLiveData(false)
}