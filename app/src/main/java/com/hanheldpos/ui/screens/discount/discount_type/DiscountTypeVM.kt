package com.hanheldpos.ui.screens.discount.discount_type

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountTypeVM : BaseUiViewModel<DiscountTypeUV>() {
    var typeDiscountSelect = MutableLiveData<DiscountTypeFor>();
}