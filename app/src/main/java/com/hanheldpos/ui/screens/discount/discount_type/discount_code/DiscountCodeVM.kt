package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CouponDiscountReq
import com.hanheldpos.data.api.pojo.discount.CouponDiscountResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData<Boolean>(false);
    private val discountRepo = DiscountRepo()
    fun initData() {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        if (listDiscountCode != null) uiCallback?.loadDataDiscountCode(listDiscountCode as List<DiscountResp>);
    }

    fun searchDiscountCode(keyword: String) {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic };
        val searchList =  listDiscountCode?.filter { it.DiscountCode?.lowercase()!!.contains(keyword.lowercase()) }
        uiCallback?.loadDataDiscountCode(searchList as List<DiscountResp>);
    }


    fun onApplyCouponCode(couponCode: String) {
        val body = GSonUtils.toServerJson(CartConverter.toOrderCoupon(CurCartData.cartModel!!, couponCode))
        showLoading(true)
        discountRepo.postNumberIncreamentAsync( body, object : BaseRepoCallback<BaseResponse<CouponDiscountResp>?>{
            override fun apiResponse(data: BaseResponse<CouponDiscountResp>?) {
                showLoading(false)
                if (data == null || data.DidError) {
                    showError(data?.ErrorMessage ?: PosApp.instance.getString(R.string.invalid_discount));
                } else {
                    if(data.Model != null)
                    uiCallback?.updateDiscountCouponCode(data.Model)
                    else {
                        showError(PosApp.instance.getString(R.string.already_apply_discount))
                    }
                }
            }

            override fun showMessage(message: String?) {
                showLoading(false)
                showError(message)
            }
        })
    }
}