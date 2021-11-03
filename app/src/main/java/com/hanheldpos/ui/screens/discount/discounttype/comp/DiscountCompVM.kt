package com.hanheldpos.ui.screens.discount.discounttype.comp

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.order.settings.ListCompItem
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountCompVM : BaseUiViewModel<DiscountCompUV>() {
    val reasonChosen = MutableLiveData<ListReasonsItem>();
}