package com.hanheldpos.ui.screens.combo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.model.product.BaseProductInCart
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

    var mLastTimeClick: Long = 0

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        bundleInCart.observe(owner, {
            updateTotalPrice();
        });
    }

    fun initDefaultComboList(
        listGroup: MutableList<GroupBundle>,
        diningOption: DiningOptionItem,
        menuOrderId : String
    ) {

        bundleInCart.value!!.let { combo ->
            listGroup.map { group ->
                val listRegular: List<Regular> =
                    OrderMenuDataMapper.getProductItemListByComboGuid(group.comboInfo.comboGuid)
                        .map {
                            var priceOverride =
                                it.priceOverride(menuOrderId, it.skuDefault, it.price?: 0.0);
                            val regular = Regular(it, diningOption, 1, it.skuDefault, it.variants,priceOverride,null)
                            regular.priceOverride = regular.groupPrice(group,combo.proOriginal!!);
                            return@map regular
                        }
                ItemComboGroup(
                    groupBundle = group,
                    productsForChoose = listRegular
                )
            }.let {
                uiCallback?.onLoadComboSuccess(it);
            }
        }


    }

    fun onRegularSelect(group : GroupBundle,item: Regular, action: ItemActionType){
        when(action){
            ItemActionType.Add -> group.addRegular(item);
            ItemActionType.Modify -> TODO()
            ItemActionType.Remove -> TODO()
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