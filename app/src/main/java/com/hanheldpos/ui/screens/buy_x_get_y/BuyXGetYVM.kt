package com.hanheldpos.ui.screens.buy_x_get_y

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.buy_x_get_y.GroupType
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYVM : BaseUiViewModel<BuyXGetYUV>() {
    var buyXGetY = MutableLiveData<BuyXGetY>()

    var mLastTimeClick: Long = 0
    fun getBack() {
        uiCallback?.onFragmentBackPressed()
    }

    fun initDefaultList(disc: DiscountResp): MutableList<ItemBuyXGetYGroup>? {
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
        )

        for (index in 0..1)
            groupList.add(
                GroupBuyXGetY(
                    parentDisc_Id = disc._id,
                    condition = if (index == 0) disc.Condition.CustomerBuys else disc.Condition.CustomerGets,
                    type = if (index == 0) GroupType.BUY else GroupType.GET
                )
            )

        val listGroupBuyXGetY = buyXGetY.value!!.clone()
        listGroupBuyXGetY.groupList = groupList

        listGroupBuyXGetY.groupList?.map { groupBuyXGetY ->
            ItemBuyXGetYGroup(groupBuyXGetY)
        }.let {
            buyXGetY.notifyValueChange()
            return it?.toMutableList()
        }

    }

    fun onRegularSelect(
        group: GroupBuyXGetY,
        itemPrev: Regular,
        itemAfter: Regular,
        action: ItemActionType,
        disc: DiscountResp
    ) {
        when (action) {
            ItemActionType.Add -> group.addProduct(
                disc,
                itemAfter.proOriginal!!,
                CurCartData.cartModel?.diningOption!!
            );
            ItemActionType.Modify -> {
                if (itemAfter.quantity ?: 0 <= 0) {
                    group.productList.toMutableList().remove(itemPrev);
                } else {
                    group.productList.find { it == itemPrev }.let { regular ->
                        val index = group.productList.indexOf(regular)
                        group.productList.toMutableList()[index] = itemAfter;
                    }
                }

            }
            ItemActionType.Remove -> {
                group.productList.toMutableList().remove(itemPrev);
            }
        }
        buyXGetY.notifyValueChange();

    }

    fun isSelectedComplete(): Boolean {
        return buyXGetY.value?.isCompleted() ?: false
    }
}