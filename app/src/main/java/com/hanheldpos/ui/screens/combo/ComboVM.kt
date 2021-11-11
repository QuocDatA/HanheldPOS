package com.hanheldpos.ui.screens.combo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ComboVM : BaseUiViewModel<ComboUV>() {


    val orderItemModel = MutableLiveData<OrderItemModel>();
    val actionType = MutableLiveData<ItemActionType>();
    val numberQuantity = Transformations.map(orderItemModel) {
        return@map it.quantity;
    };
    var maxQuantity = -1;
    var minQuantity: LiveData<Int> = Transformations.map(actionType) {
        return@map when (actionType.value) {
            ItemActionType.Modify -> 0;
            ItemActionType.Add -> 1;
            else -> 1;
        };
    };
    val totalPriceLD = MutableLiveData(0.0);

    /**
     *  List name of selected products in combo list
     */

    val selectedCombo = MutableLiveData<OrderMenuComboItemModel>()
    val isSelectedComplete: MutableLiveData<Boolean> = Transformations.map(selectedCombo) {
        val result = it?.listItemsByGroup?.sumOf {
            it?.requireQuantity() ?: 0
        }
        return@map result == 0;
    } as MutableLiveData<Boolean>

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        selectedCombo.observe(owner, {
            uiCallback?.updateChangeCombo(it);
            updateTotalPrice();
        });
        orderItemModel.observe(owner, {
            updateTotalPrice();
        });
    }

    fun initDefaultComboList(
        listProductComboItem: MutableList<ProductComboItem>
    ) {
        var comboMenuComboItemModel = orderItemModel.value?.menuComboItem;
        if (comboMenuComboItemModel == null){
            //Default requirements for selected product combo
            val comboGroupList: MutableList<ItemComboGroupManager?> = mutableListOf()
            listProductComboItem.map { productComboItem ->
                // Get Group price folow combo group
//                val productParent = orderItemModel.value?.productOrderItem;
//                val groupPrice =
//                    productParent?.listGroupPriceInCombo?.find { it.groupGUID == productComboItem.comboGuid };
//                comboGroupList.add(
//                    ItemComboGroupManager(
//                        productComboItem = productComboItem,
//                    ).apply {
//
//                    }
//                )
            }
            val comboId: String = GenerateId.getOrderItemId()
            comboMenuComboItemModel = OrderMenuComboItemModel(
                listItemsByGroup = comboGroupList,
                comboParentId = comboId,
            )
        }

        selectedCombo.value = comboMenuComboItemModel;
        if (orderItemModel.value!!.menuComboItem == null)
            orderItemModel.value!!.menuComboItem = selectedCombo.value;
    }

    fun onChooseItemComboSuccess(
        comboParent: String?,
        comboManager: ItemComboGroupManager,
        item: ComboPickedItemViewModel,
        action: ItemActionType?
    ) {
        selectedCombo.value?.let { orderMenuComboItemModel ->
            orderMenuComboItemModel.listItemsByGroup?.forEach { it1 ->
                if (it1?.productComboItem?.comboGuid == comboParent) {
                    /**
                     * show check icon when add or modify item
                     */
                    when (action) {
                        ItemActionType.Add -> {
                            /*
                            * If in combo has the same item -> increase quantity
                            * */
                            if (it1?.listSelectedComboItems!!.contains(item)) {
                                it1.listSelectedComboItems.let {
                                    it[it.indexOf(item)]?.apply {
                                        selectedComboItem!!.plusOrderQuantity(item.selectedComboItem!!.quantity);
                                    }
                                }
                            } else
                                it1.listSelectedComboItems.add(item)
                        }
                        ItemActionType.Modify -> {
                            if (it1?.listSelectedComboItems!!.contains(item)) {
                                it1.listSelectedComboItems.let {
                                    it.set(it.indexOf(item), item);
                                }
                            }
                        }
                        ItemActionType.Remove -> {
                            /**
                             * Delete green tick when remove item
                             */
                            comboManager.let {
                                it.listSelectedComboItems.remove(item);
                            }
                        }
                    }
                    selectedCombo.notifyValueChange()
                    return;
                }
            }
        }
    }

    private fun updateTotalPrice() {
        totalPriceLD.value = orderItemModel.value?.getPriceLineTotal();
    }

    fun getCombo(): MutableList<ProductComboItem>? {
//        return orderItemModel.value?.productOrderItem?.productComboList;
        return  mutableListOf();
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity)
            orderItemModel.value?.plusOrderQuantity(1);
        orderItemModel.notifyValueChange();
    }

    fun onRemoveQuantity() {
        if (minQuantity.value!! < numberQuantity.value!!)
            orderItemModel.value?.minusOrderQuantity(1);
        orderItemModel.notifyValueChange();
    }

    fun onAddCart() {
        uiCallback?.cartAdded(orderItemModel.value!!, actionType.value!!);
    }

    fun onBack() {
        uiCallback?.onBack();
    }

}