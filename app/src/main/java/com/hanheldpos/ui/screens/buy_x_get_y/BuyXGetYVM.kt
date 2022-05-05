package com.hanheldpos.ui.screens.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.cart.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.cart.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.cart.buy_x_get_y.GroupType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYVM : BaseUiViewModel<BuyXGetYUV>() {
    private lateinit var buyXGetY: BuyXGetY
    fun getBack() {
        uiCallback?.onFragmentBackPressed()
    }

    fun initDefaultList(disc: DiscountResp): BuyXGetY {
        val groupList: MutableList<GroupBuyXGetY> = mutableListOf()

        for (index in 0..1)
            groupList.add(
                GroupBuyXGetY(
                    parentDisc_Id = disc._id,
                    condition = if (index == 0) disc.Condition.CustomerBuys else disc.Condition.CustomerGets,
                    type = if (index == 0) GroupType.BUY else GroupType.GET
                )
            )

        buyXGetY =
            BuyXGetY(
                discount = disc,
                null,
                null,
                diningOption = CurCartData.cartModel?.diningOption!!,
                quantity = 1,
                null,
                null,
                null,
                null,
                groupList = groupList
            )

        return buyXGetY
    }

    fun isSelectedComplete() {

    }
}