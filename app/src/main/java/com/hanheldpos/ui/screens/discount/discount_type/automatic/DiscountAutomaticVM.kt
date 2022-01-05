package com.hanheldpos.ui.screens.discount.discount_type.automatic

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import java.util.*

class DiscountAutomaticVM : BaseUiViewModel<DiscountAutomaticUV>() {

    val isLoading = MutableLiveData<Boolean>(false);

    fun loadDiscountAutomatic(item: BaseProductInCart? = null, cart: CartModel? = null) {
        if (item != null) {
            val listDiscountCode = DataHelper.discountResp?.Model?.filter {
                !it.DiscountAutomatic && it.DiscountApplyTo == DiscountApplyToType.ITEM_DISCOUNT_APPLY_TO.value && cart?.customer?.let { it1 ->
                    it.isValid(
                        item,
                        it1,
                        Date()
                    )
                } == true
            };
            if (listDiscountCode != null) {
                uiCallback?.loadDataDiscountCode(listDiscountCode)
            };
        }
        if (cart != null) {
            val listDiscountCode = DataHelper.discountResp?.Model?.filter {
                !it.DiscountAutomatic && it.DiscountApplyTo == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO.value && it.isValid(
                    cart,
                    Date()
                )
            };
            if (listDiscountCode != null) {
                uiCallback?.loadDataDiscountCode(listDiscountCode)
                return;
            };
        }

    }

}