package com.hanheldpos.ui.screens.menu.discount

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

class MenuDiscountVM : BaseUiViewModel<MenuDiscountUV>() {
    private val discountRepo = DiscountRepo()
    fun onFragmentBackPress() {
        uiCallback?.onFragmentBackPressed()
    }

    fun initListDiscountCode() {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic }
        val sortList = listDiscountCode?.sortedBy { it.DiscountName }
        uiCallback?.loadDiscountCode(sortList ?: listOf())
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
                if (discSelected.isMaxNumberOfUsedPerOrder()) {
                    showError(PosApp.instance.getString(R.string.msg_error_maximum_selection_is_))
                    return
                }
                uiCallback?.openDiscountBuyXGetY(discSelected)
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
                        )
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

    fun onScanner() {
        uiCallback?.onScanner()
    }

    fun onScanDiscountSuccess(discountCode: String) {
        val discountScan: DiscountResp? =
            DataHelper.discountsLocalStorage?.find { disc -> disc.DiscountCode == discountCode }
        if (discountScan != null) {
            onApplyCouponCode(discountCode)
        } else {
            showError(PosApp.instance.getString(R.string.invalid_discount))
        }
    }
}