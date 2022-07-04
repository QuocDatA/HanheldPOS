package com.hanheldpos.ui.screens.discount

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
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils
import java.util.*

class DiscountVM : BaseUiViewModel<DiscountUV>() {

    var typeDiscountSelect = MutableLiveData<DiscountTypeFor>();
    private var discountRepo = DiscountRepo()

    fun backPress() {
        uiCallback?.backPress()
    }

    fun onScanner() {
        uiCallback?.onScanner()
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

    fun isScannableTypeDiscount(): Boolean {
        return when (typeDiscountSelect.value) {
            DiscountTypeFor.DISCOUNT_CODE -> {
                true
            }
            DiscountTypeFor.AUTOMATIC -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun onApplyDiscountAuto(discount: DiscountResp) {
        // Re-check the validity of the discount.
        if (discount.isBuyXGetY() || !discount.isValid(CurCartData.cartModel!!, Date()) ?: false) {
            showError(PosApp.instance.getString(R.string.invalid_discount))
            return;
        }

        // Check limit for discount.
        if (discount.isMaxNumberOfUsedPerOrder()) {
            showError(PosApp.instance.getString(R.string.maxium_usage_limited))
            return;
        }

        when (DiscApplyTo.fromInt(discount.DiscountApplyTo)) {
            DiscApplyTo.ITEM -> {
                uiCallback?.onApplyDiscountAuto(discount, DiscApplyTo.ITEM)
            }
            DiscApplyTo.ORDER -> {
                uiCallback?.onApplyDiscountAuto(discount, DiscApplyTo.ORDER)
            }
            else -> {}
        }
    }

    fun onScanDiscount(discountCode: String) {
        var discountScan : DiscountResp? = null
        if(typeDiscountSelect.value  ==  DiscountTypeFor.DISCOUNT_CODE) {
            discountScan =
                DataHelper.discountsLocalStorage?.find { disc -> !disc.DiscountAutomatic && disc.DiscountCode == discountCode };
        } else if(typeDiscountSelect.value == DiscountTypeFor.AUTOMATIC) {
            discountScan =
                DataHelper.discountsLocalStorage?.find { disc -> disc.DiscountAutomatic && disc.DiscountCode == discountCode };
        }

        if (discountScan != null) {
            when (typeDiscountSelect.value) {
                DiscountTypeFor.DISCOUNT_CODE -> {
                    when (DiscountTypeEnum.fromInt(discountScan.DiscountType)) {
                        DiscountTypeEnum.BUYX_GETY -> {
                            onApplyCouponCode(discountCode)
                        }
                        DiscountTypeEnum.AMOUNT -> {
                            onApplyCouponCode(discountCode)
                        }
                        DiscountTypeEnum.PERCENT -> {
                            onApplyCouponCode(discountCode)
                        }
                        else -> {}
                    }
                }
                DiscountTypeFor.AUTOMATIC -> {
                    onApplyDiscountAuto(discountScan)
                }
                else -> {}
            }
        } else {
            showError(PosApp.instance.getString(R.string.invalid_discount))
        }
    }
}