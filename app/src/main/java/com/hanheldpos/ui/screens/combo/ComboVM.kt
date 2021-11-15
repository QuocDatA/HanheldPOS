package com.hanheldpos.ui.screens.combo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ComboVM : BaseUiViewModel<ComboUV>() {

    val bundleInCart = MutableLiveData<Combo>();
    val actionType = MutableLiveData<ItemActionType>();
    val numberQuantity = Transformations.map(bundleInCart) {
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


    val isSelectedComplete: MutableLiveData<Boolean> = Transformations.map(bundleInCart) {
        return@map it?.isCompleted();
    } as MutableLiveData<Boolean>

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        bundleInCart.observe(owner, {
            updateTotalPrice();
        });
    }

    fun initDefaultComboList(
        listGroup: MutableList<GroupBundle>
    ) {

        listGroup.map { group ->
            ItemComboGroup(
                groupBundle = group,
                productsForChoose = OrderMenuDataMapper.getProductItemListByComboGuid(group.comboInfo.comboGuid).map { ProductMenuItem(it) }
            )
        }.let {
            uiCallback?.onLoadComboSuccess(it);
        }

    }

//    fun onChooseItemComboSuccess(
//        comboParent: String?,
//        comboManager: ItemComboGroupManager,
//        item: ComboPickedItemViewModel,
//        action: ItemActionType?
//    ) {
//        selectedCombo.value?.let { orderMenuComboItemModel ->
//            orderMenuComboItemModel.listItemsByGroup?.forEach { it1 ->
//                if (it1?.productComboItem?.comboGuid == comboParent) {
//                    /**
//                     * show check icon when add or modify item
//                     */
//                    when (action) {
//                        ItemActionType.Add -> {
//                            /*
//                            * If in combo has the same item -> increase quantity
//                            * */
//                            if (it1?.listSelectedComboItems!!.contains(item)) {
//                                it1.listSelectedComboItems.let {
//                                    it[it.indexOf(item)]?.apply {
//                                        selectedComboItem!!.plusOrderQuantity(item.selectedComboItem!!.quantity);
//                                    }
//                                }
//                            } else
//                                it1.listSelectedComboItems.add(item)
//                        }
//                        ItemActionType.Modify -> {
//                            if (it1?.listSelectedComboItems!!.contains(item)) {
//                                it1.listSelectedComboItems.let {
//                                    it.set(it.indexOf(item), item);
//                                }
//                            }
//                        }
//                        ItemActionType.Remove -> {
//                            /**
//                             * Delete green tick when remove item
//                             */
//                            comboManager.let {
//                                it.listSelectedComboItems.remove(item);
//                            }
//                        }
//                    }
//                    selectedCombo.notifyValueChange()
//                    return;
//                }
//            }
//        }
//    }

    private fun updateTotalPrice() {
        totalPriceLD.value = bundleInCart.value?.total()
    }

    fun getCombo(): MutableList<GroupBundle>? {
        return bundleInCart.value?.groupList;
    }

    fun onAddQuantity() {
//        if (numberQuantity.value!! < maxQuantity)
//            orderItemModel.value?.plusOrderQuantity(1);
//        orderItemModel.notifyValueChange();
    }

    fun onRemoveQuantity() {
//        if (minQuantity.value!! < numberQuantity.value!!)
//            orderItemModel.value?.minusOrderQuantity(1);
//        orderItemModel.notifyValueChange();
    }

    fun onAddCart() {
//        uiCallback?.cartAdded(orderItemModel.value!!, actionType.value!!);
    }

    fun onBack() {
        uiCallback?.onBack();
    }

}