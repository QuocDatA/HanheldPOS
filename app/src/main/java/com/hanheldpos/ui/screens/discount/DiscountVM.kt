package com.hanheldpos.ui.screens.discount

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils

class DiscountVM : BaseUiViewModel<DiscountUV>() {

    var typeDiscountSelect = MutableLiveData<DiscountTypeFor>();
    private var discountRepo = DiscountRepo()

    fun backPress(){
        uiCallback?.backPress();
    }

    fun onScanQrCode() {
        uiCallback?.onScanQrCode()
    }

    fun onApplyCouponCode(couponCode: String) {
        val body =
            GSonUtils.toServerJson(CartConverter.toOrder(CurCartData.cartModel!!, couponCode))
        showLoading(true)
        discountRepo.postDiscountCoupon(
            body,
            object : BaseRepoCallback<BaseResponse<List<DiscountCoupon>>?> {
                override fun apiResponse(data: BaseResponse<List<DiscountCoupon>>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(
                            data?.ErrorMessage
                                ?: PosApp.instance.getString(R.string.invalid_discount)
                        );
                    } else {
                        uiCallback?.onApplyDiscountCode(data.Model ?: mutableListOf())
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }
}