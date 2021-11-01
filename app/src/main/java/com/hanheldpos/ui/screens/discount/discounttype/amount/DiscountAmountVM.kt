package com.hanheldpos.ui.screens.discount.discounttype.amount

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountAmountVM : BaseUiViewModel<DiscountAmountUV>() {
    val amount = MutableLiveData<String>(0.0.toString());
    val title = MutableLiveData<String>();
}