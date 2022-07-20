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
import com.hanheldpos.model.product.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.product.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.product.buy_x_get_y.GroupType
import com.hanheldpos.model.product.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.StringUtils

class DiscountCodeVM : BaseUiViewModel<DiscountCodeUV>() {

    val isLoading = MutableLiveData(false)
    var buyXGetY = MutableLiveData<BuyXGetY>()
    private val discountRepo = DiscountRepo()
    fun initData() {
        searchDiscountCode()
    }

    fun searchDiscountCode(keyword: String = "") {
        val listDiscountCode = DataHelper.discountsLocalStorage?.filter { !it.DiscountAutomatic }
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
                if (discSelected.isMaxNumberOfUsedPerOrder()) {
                    showError(PosApp.instance.getString(R.string.msg_error_maximum_selection_is_))
                    return
                }
                uiCallback?.openBuyXGetY(discSelected)
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
                            StringUtils.htmlParse(
                                data?.ErrorMessage
                                    ?: PosApp.instance.getString(R.string.invalid_discount)
                            )
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

    fun initDefaultBuyXGetY(disc: DiscountResp): BuyXGetY {
        val groupList: MutableList<GroupBuyXGetY> = mutableListOf()

        buyXGetY.value = BuyXGetY(
            discount = disc,
            null,
            null,
            diningOption = CurCartData.cartModel?.diningOption!!,
            quantity = 1,
            null,
            null,
            null,
            null,
            null
        )

        for (index in 0..1)
            groupList.add(
                GroupBuyXGetY(
                    parentDisc_Id = disc._id,
                    condition = if (index == 0) disc.Condition.CustomerBuys else disc.Condition.CustomerGets,
                    type = if (index == 0) GroupType.BUY else GroupType.GET
                )
            )

        buyXGetY.value!!.groupList = groupList

        return buyXGetY.value!!
    }
}