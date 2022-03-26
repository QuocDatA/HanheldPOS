package com.hanheldpos.ui.screens.discount.discount_type.automatic

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.discount.DiscountApplyTo
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.concatenate
import java.util.*

class DiscountAutomaticVM : BaseUiViewModel<DiscountAutomaticUV>() {

    val isLoading = MutableLiveData<Boolean>(false);

    fun loadDiscountAutomatic()  {
        val listDiscountAutoItem = DataHelper.findDiscountAutoList(DiscountApplyTo.ITEM)
        val listDiscountAutoOrder = DataHelper.findDiscountAutoList(DiscountApplyTo.ORDER)
        uiCallback?.loadDataDiscountCode(concatenate(listDiscountAutoItem,listDiscountAutoOrder))
    }



}