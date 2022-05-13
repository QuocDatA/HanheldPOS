package com.hanheldpos.ui.screens.buy_x_get_y

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.buy_x_get_y.GroupType
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYVM : BaseUiViewModel<BuyXGetYUV>() {
    var buyXGetY = MutableLiveData<BuyXGetY>()
    var listGroupBuyXGetY = MutableLiveData<BuyXGetY>()
    var actionType = MutableLiveData<ItemActionType>();
    val totalPriceLD = MutableLiveData(0.0);
    var maxQuantity = -1;
    var minQuantity: LiveData<Int> = Transformations.map(actionType) {
        return@map when (actionType.value) {
            ItemActionType.Modify -> 0;
            ItemActionType.Add -> 1;
            else -> 1;
        };
    };

    var mLastTimeClick: Long = 0
    fun getBack() {
        uiCallback?.onFragmentBackPressed()
    }

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        listGroupBuyXGetY.observe(owner) {
            updateTotalPrice();
        };
    }

    private fun updateTotalPrice() {
        totalPriceLD.value = listGroupBuyXGetY.value?.total()
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

        listGroupBuyXGetY.value = buyXGetY.value!!.clone()
        listGroupBuyXGetY.value!!.groupList = groupList

        listGroupBuyXGetY.value!!.groupList?.map { groupBuyXGetY ->
            initItemGroupBuyXGetY(groupBuyXGetY)
        }.let {
            return it?.toMutableList()
        }

    }

    fun onRegularSelect(
        group: GroupBuyXGetY,
        itemPrev: Regular,
        itemAfter: Regular,
        action: ItemActionType,
    ) {
        when (action) {
            ItemActionType.Add -> {
                group.productList.add(itemAfter)
            }
            ItemActionType.Modify -> {
                if (itemAfter.quantity ?: 0 <= 0) {
                    group.productList.remove(itemPrev);
                } else {
                    group.productList.find { it == itemPrev }.let { regular ->
                        val index = group.productList.indexOf(regular)
                        group.productList[index] = itemAfter;
                    }
                }

            }
            ItemActionType.Remove -> {
                group.productList.remove(itemPrev);
            }
        }
        buyXGetY.notifyValueChange();
    }

    val isSelectedComplete: MutableLiveData<Boolean> = Transformations.map(listGroupBuyXGetY) {
        return@map it?.isCompleted();
    } as MutableLiveData<Boolean>

    val numberQuantity = Transformations.map(listGroupBuyXGetY) {
        return@map it.quantity;
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity)
            listGroupBuyXGetY.value?.plusOrderQuantity(1);
        listGroupBuyXGetY.notifyValueChange()
    }

    fun onRemoveQuantity() {
        if (minQuantity.value!! < numberQuantity.value!!)
            listGroupBuyXGetY.value?.minusOrderQuantity(1);
        listGroupBuyXGetY.notifyValueChange();
    }

    fun onAddCart() {
        //uiCallback?.cartAdded(bundleInCart.value!!, actionType.value!!);
    }

    private fun initItemGroupBuyXGetY(groupBuyXGetY: GroupBuyXGetY): ItemBuyXGetYGroup {
        val conditionCustomer = groupBuyXGetY.condition
        val itemBuyXGetYGroup = ItemBuyXGetYGroup(groupBuyXGetY, mutableListOf(), mutableListOf())
        if (conditionCustomer is CustomerBuys) {
            itemBuyXGetYGroup.groupListRegular =
                conditionCustomer.filterListApplyTo(itemBuyXGetYGroup)
            itemBuyXGetYGroup.listApplyTo = conditionCustomer.ListApplyTo.toMutableList()
        } else {
            conditionCustomer as CustomerGets
            itemBuyXGetYGroup.groupListRegular =
                conditionCustomer.filterListApplyTo(itemBuyXGetYGroup)
            itemBuyXGetYGroup.listApplyTo = conditionCustomer.ListApplyTo.toMutableList()
        }
        return itemBuyXGetYGroup
    }
}