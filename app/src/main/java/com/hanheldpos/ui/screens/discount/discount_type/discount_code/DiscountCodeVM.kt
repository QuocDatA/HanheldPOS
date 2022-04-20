package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData<Boolean>(false);
    private val discountRepo = DiscountRepo()
    fun initData() {
        searchDiscountCode()
    }

    fun searchDiscountCode(keyword: String = "") {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        val searchList = listDiscountCode?.sortedBy { it.DiscountName }
            ?.filter { it.DiscountCode.uppercase().contains(keyword.uppercase()) }
        searchList?.let {
            uiCallback?.loadDataDiscountCode(searchList)
        }
    }

    fun onApplyDiscount(discSelected: DiscountResp) {
        when (DiscountTypeEnum.fromInt(discSelected.DiscountType)) {
            DiscountTypeEnum.PERCENT -> {
                onApplyCouponCode(discSelected.DiscountCode)
            }
            DiscountTypeEnum.AMOUNT -> {
                onApplyCouponCode(discSelected.DiscountCode)
            }
            DiscountTypeEnum.BUYX_GETY -> {

            }
            else -> {}
        }
    }

    private fun onApplyCouponCode(couponCode: String) {
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
                            uiCallback?.updateDiscountCouponCode(data.Model)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }
}