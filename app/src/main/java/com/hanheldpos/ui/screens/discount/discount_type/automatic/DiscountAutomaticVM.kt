package com.hanheldpos.ui.screens.discount.discount_type.automatic

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.concatenate
import java.util.*

class DiscountAutomaticVM : BaseUiViewModel<DiscountAutomaticUV>() {

    val isLoading = MutableLiveData(false)

    fun initData() {
        loadDiscountAutomatic()
    }

    fun loadDiscountAutomatic(keyword : String = "")  {
        val listDiscountAutoItem = DataHelper.findDiscountAutoList(DiscApplyTo.ITEM)
        val listDiscountAutoOrder = DataHelper.findDiscountAutoList(DiscApplyTo.ORDER)
        uiCallback?.loadDataDiscountCode(concatenate(listDiscountAutoItem,listDiscountAutoOrder).sortedBy { it.DiscountName }.filter { it.DiscountCode.uppercase().contains(keyword.uppercase()) })
    }



    fun onApplyDiscountAuto(discount : DiscountResp) {
        // Re-check the validity of the discount.
        if (discount.isBuyXGetY() || !discount.isValid(CurCartData.cartModel!!, Date())) {
            showError(PosApp.instance.getString(R.string.invalid_discount))
            return
        }

        // Check limit for discount.
        if (discount.isMaxNumberOfUsedPerOrder()) {
            showError(PosApp.instance.getString(R.string.maxium_usage_limited))
            return
        }

        when (DiscApplyTo.fromInt(discount.DiscountApplyTo)) {
            DiscApplyTo.ITEM -> {
                uiCallback?.onApplyDiscountForItem(discount)
            }
            DiscApplyTo.ORDER -> {
                uiCallback?.onApplyDiscountForOrder(discount)
            }
            else -> {}
        }
    }


}