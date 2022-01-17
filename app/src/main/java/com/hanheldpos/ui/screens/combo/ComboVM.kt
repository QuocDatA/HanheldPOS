package com.hanheldpos.ui.screens.combo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ComboVM : BaseUiViewModel<ComboUV>() {

    val isValidDiscount = MutableLiveData<Boolean>(false);
    var typeDiscountSelect : DiscountTypeFor?= null;

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

    var mLastTimeClick: Long = 0

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        bundleInCart.observe(owner, {
            updateTotalPrice();
        });
    }

    fun initDefaultComboList(
        listGroup: MutableList<GroupBundle>,
        diningOption: DiningOption,
        menuOrderId : String
    ) {

        bundleInCart.value!!.let { combo ->
            listGroup.map { group ->
                val listRegular: List<Regular> = group.productsForChoose(menuResp = DataHelper.menuResp!!,menuOrderId,diningOption,combo.proOriginal!!);
                ItemComboGroup(
                    groupBundle = group,
                    productsForChoose = listRegular
                )
            }.let {
                uiCallback?.onLoadComboSuccess(it);
            }
        }


    }

    fun onRegularSelect(group : GroupBundle,itemPrev: Regular,itemAfter : Regular, action: ItemActionType){
        when(action){
            ItemActionType.Add -> group.addRegular(itemAfter);
            ItemActionType.Modify -> {
                group.productList.find { it == itemPrev }.let { regular ->
                    val index = group.productList.indexOf(regular)
                    group.productList[index] = itemAfter;
                }
            }
            ItemActionType.Remove ->{
                group.productList.remove(itemPrev);
            }
        }
        bundleInCart.notifyValueChange();

    }

    private fun updateTotalPrice() {
        totalPriceLD.value = bundleInCart.value?.total()
    }

    fun getCombo(): MutableList<GroupBundle>? {
        return bundleInCart.value?.groupList;
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity)
            bundleInCart.value?.plusOrderQuantity(1);
        bundleInCart.notifyValueChange()
    }

    fun onRemoveQuantity() {
        if (minQuantity.value!! < numberQuantity.value!!)
            bundleInCart.value?.minusOrderQuantity(1);
        bundleInCart.notifyValueChange();
    }

    fun onAddCart() {
        uiCallback?.cartAdded(bundleInCart.value!!, actionType.value!!);
    }

    fun onBack() {
        uiCallback?.onBack();
    }

}