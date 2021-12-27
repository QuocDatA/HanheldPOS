package com.hanheldpos.ui.screens.product.temporary_style

import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TemporaryStyleVM : BaseUiViewModel<TemporaryStyleUV>() {

    fun initSauceItem(): List<TemporarySauceItem> {
        val sauceItemList = mutableListOf<TemporarySauceItem>(
            TemporarySauceItem("Pepper Sauce"),
            TemporarySauceItem("Mushroom Sauce"),
            TemporarySauceItem("Tuna Sauce"),
            TemporarySauceItem("BBQ Sauce"),
        )
        return sauceItemList
    }

    fun backPress() {
        uiCallback?.getBack()
    }
}