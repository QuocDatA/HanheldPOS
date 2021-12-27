package com.hanheldpos.ui.screens.product.temporary_style

import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TemporaryStyleVM : BaseUiViewModel<TemporaryStyleUV>() {

    fun backPress() {
        uiCallback?.getBack()
    }
}