package com.hanheldpos.ui.screens.discount.discounttype

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountTypeVM : BaseUiViewModel<DiscountTypeUV>() {
    val reasonChosen = MutableLiveData<Reason?>();
}