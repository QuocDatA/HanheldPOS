package com.hanheldpos.ui.screens.buy_x_get_y

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.buy_x_get_y.*
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYVM : BaseUiViewModel<BuyXGetYUV>() {
    var buyXGetY = MutableLiveData<BuyXGetY>()
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

        buyXGetY.observe(owner) {
            updateTotalPrice();
        };
    }

    private fun updateTotalPrice() {
        totalPriceLD.value = buyXGetY.value?.total()
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

        buyXGetY.value!!.groupList = groupList

        buyXGetY.value!!.groupList?.map { groupBuyXGetY ->
            initItemGroupBuyXGetY(groupBuyXGetY, isBuyComplete())
        }.let {
            return it?.toMutableList()
        }

    }

    private fun isBuyComplete(): Boolean {
        val applyTo = buyXGetY.value!!.disc?.Condition?.CustomerBuys?.ApplyTo ?: 0
        when (CustomerDiscApplyTo.fromInt(applyTo)) {
            CustomerDiscApplyTo.ENTIRE_ORDER -> {
                val totalOrder = CurCartData.cartModel!!.total()
                val totalQuantityOrder = CurCartData.cartModel!!.getTotalQuantity()

                if(!(buyXGetY.value!!.disc?.Condition?.CustomerBuys?.isBuyCompleted(totalOrder, totalQuantityOrder))!!) {

                } else {
                    return true
                }
            }
            else -> {

            }
        }
        return false
    }

    fun onRegularSelect(
        group: GroupBuyXGetY,
        itemPrev: Regular,
        itemAfter: Regular,
        action: ItemActionType,
        discount: DiscountResp,
    ) {
        when (action) {
            ItemActionType.Add -> {
                itemAfter.addDiscountServer(discount)
                group.productList.add(itemAfter)
            }
            ItemActionType.Modify -> {
                if ((itemAfter.quantity ?: 0) <= 0) {
                    group.productList.remove(itemPrev);
                } else {
                    group.productList.find { it == itemPrev }?.let { regular ->
                        val index = group.productList.indexOf(regular)
                        group.productList[index] = itemAfter;
                    }
                }

            }
            ItemActionType.Remove -> {
                group.productList.remove(itemPrev);
            }
        }
    }

    fun onBundleSelect(
        group: GroupBuyXGetY,
        item: Combo,
        action: ItemActionType,
        discount: DiscountResp,
    ) {
        when (action) {
            ItemActionType.Add -> {
                group.productList.add(item)
            }
            ItemActionType.Modify -> {
                if ((item.quantity ?: 0) <= 0) {
                    group.productList.remove(item);
                } else {
                    group.productList.find { it.proOriginal?._id == item.proOriginal?._id }?.let { regular ->
                        val index = group.productList.indexOf(regular)
                        group.productList[index] = item;
                    }
                }

            }
            ItemActionType.Remove -> {
                group.productList.remove(item);
            }
        }
    }

    val isSelectedComplete: MutableLiveData<Boolean> = Transformations.map(buyXGetY) {
        return@map it?.isCompleted();
    } as MutableLiveData<Boolean>

    val numberQuantity = Transformations.map(buyXGetY) {
        return@map it.quantity;
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity)
            buyXGetY.value?.plusOrderQuantity(1);
        buyXGetY.notifyValueChange()
    }

    fun onRemoveQuantity() {
        if (minQuantity.value!! < numberQuantity.value!!)
            buyXGetY.value?.minusOrderQuantity(1);
        buyXGetY.notifyValueChange();
    }

    fun onAddCart() {
        uiCallback?.cartAdded(buyXGetY.value!!, actionType.value!!);
    }

    private fun initItemGroupBuyXGetY(groupBuyXGetY: GroupBuyXGetY, isBuyComplete: Boolean): ItemBuyXGetYGroup {
        val conditionCustomer = groupBuyXGetY.condition
        val itemBuyXGetYGroup = ItemBuyXGetYGroup(groupBuyXGetY, mutableListOf(), mutableListOf())
        if (conditionCustomer is CustomerBuys) {
            itemBuyXGetYGroup.groupListBaseProduct =
                conditionCustomer.filterListApplyTo(itemBuyXGetYGroup, buyXGetY.value?.disc!!)
            itemBuyXGetYGroup.listApplyTo = conditionCustomer.ListApplyTo.toMutableList()
            itemBuyXGetYGroup.isApplyToEntireOrder = CustomerDiscApplyTo.fromInt(conditionCustomer.ApplyTo) == CustomerDiscApplyTo.ENTIRE_ORDER
            itemBuyXGetYGroup.isBuyComplete = isBuyComplete
            itemBuyXGetYGroup.isBuyComplete
        } else {
            conditionCustomer as CustomerGets
            itemBuyXGetYGroup.groupListBaseProduct =
                conditionCustomer.filterListApplyTo(itemBuyXGetYGroup, buyXGetY.value?.disc!!)
            itemBuyXGetYGroup.listApplyTo = conditionCustomer.ListApplyTo.toMutableList()
            itemBuyXGetYGroup.isApplyToEntireOrder = CustomerDiscApplyTo.fromInt(conditionCustomer.ApplyTo) == CustomerDiscApplyTo.ENTIRE_ORDER
        }
        return itemBuyXGetYGroup
    }
}