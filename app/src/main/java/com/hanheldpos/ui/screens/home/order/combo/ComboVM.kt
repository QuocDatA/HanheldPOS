package com.hanheldpos.ui.screens.home.order.combo

import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.product.GroupPriceProductItem
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.cart.order.OrderItemType
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper.getComboList
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.home.order.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.home.order.combo.adapter.ComboItemAdapter

class ComboVM : BaseUiViewModel<ComboUV>() {

    data class ComboEvent(
        var data: OrderMenuComboItemModel?
    )

    val orderItemModel = MutableLiveData<OrderItemModel>();
    val extraDoneModel = MutableLiveData<ExtraDoneModel>();
    val numberQuantity = Transformations.map(extraDoneModel) {
        return@map it.quantity;
    };
    var maxQuantity = -1;
    val totalPriceLD = MutableLiveData(0.0);

    /**
     *  List name of selected products in combo list
     */
    val selectedCombo = MutableLiveData<ComboEvent>()
    val isSelectedComplete: MutableLiveData<Boolean> = Transformations.map(selectedCombo) {
        val result = it.data?.listItemsByGroup?.sumOf {
            it?.requireQuantity() ?: 0
        }
        return@map result == 0;
    } as MutableLiveData<Boolean>

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        selectedCombo.observe(owner,{
            updateTotalPrice();
        });
    }

    fun initDefaultComboList(
        listProductComboItem: MutableList<ProductComboItem>
    ) {
        //Default requirements for selected product combo
        val comboGroupList: MutableList<ItemComboGroupManager?> = mutableListOf()
        listProductComboItem.map { productComboItem ->
            // Get Group price folow combo group
            val groupPrice = extraDoneModel.value?.productOrderItem?.listGroupPriceInCombo?.find { it.groupGUID == productComboItem.comboGuid };
            comboGroupList.add(
                ItemComboGroupManager(
                    productComboItem = productComboItem,
                ).apply {
                    comboDetailAdapter = ComboItemAdapter(
                        modeViewType = ComboItemAdapter.ComboItemViewType.ForChoose,
                        listener = object : ComboItemAdapter.ComboItemListener {
                            override fun onComboItemChoose(
                                action: ComboItemActionType,
                                item: ComboPickedItemViewModel
                            ) {
                                comboItemAction(this@apply, action, item);
                            }
                        }
                    ).apply {
                        (productComboItem.id?.let { it1 ->
                            this.submitList(extraDoneModel.value?.productOrderItem?.getComboList(
                                it1
                            )?.map { productOrderItem ->
                                // Change price product folow group price
                                productOrderItem.apply {
                                    val newProductPrice : GroupPriceProductItem? = groupPrice?.product?.find { it.productGUID == this.id };
                                    if (newProductPrice != null){
                                        sku = newProductPrice.productSKU;
                                        price = newProductPrice.productAmount;
                                        text = newProductPrice.productName;
                                        if (newProductPrice.variants.isNotEmpty()){
                                            extraData?.variantStrProductList?.first()?.group?.forEach {
                                                 newProductPrice.variants.find { newItem->  newItem.groupID == it?.groupId ?: -1 }.let { findedItem->
                                                       it?.price = findedItem?.groupAmount;
                                                 }
                                            }
                                        }
                                    }
                                }
                                ComboPickedItemViewModel(
                                    comboParentId = productComboItem.comboGuid,
                                    selectedComboItem = productOrderItem
                                )
                            })
                        })
                    }
                    comboItemSelectedAdapter = ComboItemAdapter(
                        modeViewType = ComboItemAdapter.ComboItemViewType.Choosed,
                        listener = object : ComboItemAdapter.ComboItemListener {
                            override fun onComboItemChoose(
                                action: ComboItemActionType,
                                item: ComboPickedItemViewModel
                            ) {
                                comboItemAction(this@apply, action, item);
                            }
                        }
                    )
                }
            )
        }

        val comboId: String = GenerateId.getOrderItemId()

        selectedCombo.value = ComboEvent(
            data = OrderMenuComboItemModel(
                listItemsByGroup = comboGroupList,
                comboParentId = comboId,
                comboAdapter = ComboGroupAdapter().apply {
                    submitList(comboGroupList as MutableList<ItemComboGroupManager>);
                }
            )
        )
        orderItemModel.value = OrderItemModel(
            menuComboItem = selectedCombo.value!!.data,
            productOrderItem = extraDoneModel.value?.productOrderItem,
            extraDone = extraDoneModel.value,
            type = OrderItemType.Combo
        )
    }

    private fun comboItemAction(
        comboManager: ItemComboGroupManager,
        action: ComboItemActionType,
        item: ComboPickedItemViewModel
    ) {
        when (action) {
            ComboItemActionType.Add -> {
                uiCallback?.openProductDetail(comboManager.requireQuantity(), item.copy(),action);
            }
            ComboItemActionType.Modify -> {
                uiCallback?.openProductDetail(comboManager.requireQuantity(), item,action);
            }
            ComboItemActionType.Remove -> {
                selectedCombo.value?.data?.let {
                    it.listItemsByGroup?.forEach { it1 ->
                        if (it1?.productComboItem?.comboGuid == item.comboParentId) {
                            it1?.listSelectedComboItems?.let {
                                it.remove(item)
                                (it1.comboItemSelectedAdapter as ComboItemAdapter).submitList(it);
                            }
                            (it.comboAdapter as ComboGroupAdapter).notifyDataSetChanged()
                            selectedCombo.notifyValueChange()
                            return;
                        }
                    }
                }
            }
        }
    }

    fun onChooseItemComboSuccess(
        comboParent: String?,
        item: ComboPickedItemViewModel,
        action: ComboItemActionType?
    ) {
        selectedCombo.value?.data?.let { orderMenuComboItemModel ->
            orderMenuComboItemModel.listItemsByGroup?.forEach { it1 ->
                if (it1?.productComboItem?.comboGuid == comboParent) {
                    when (action) {
                        ComboItemActionType.Add -> {
                            /*
                            * If in combo has the same item -> increase quantity
                            * */
                            if (it1?.listSelectedComboItems!!.contains(item)) {
                                it1.listSelectedComboItems.let {
                                    it[it.indexOf(item)]?.apply {
                                        extraDoneModel!!.quantity = extraDoneModel!!.quantity.plus(
                                            item.extraDoneModel!!.quantity
                                        );
                                    }
                                }
                            } else
                                it1.listSelectedComboItems.add(item)
                        }
                        ComboItemActionType.Modify -> {
                            if (it1?.listSelectedComboItems!!.contains(item)) {
                                it1.listSelectedComboItems.let {
                                    it.set(it.indexOf(item), item);
                                }
                            }
                        }
                    }
                    it1?.listSelectedComboItems.let {
                        (it1?.comboItemSelectedAdapter as ComboItemAdapter).submitList(it);
                    }
                    (orderMenuComboItemModel.comboAdapter as ComboGroupAdapter).notifyDataSetChanged()
                    selectedCombo.notifyValueChange()
                    return;
                }
            }
        }
    }

    private fun updateTotalPrice(){
        totalPriceLD.value = orderItemModel.value?.getOrderPrice();
    }

    fun getCombo(): MutableList<ProductComboItem>? {
        return extraDoneModel.value?.productOrderItem?.productComboList;
    }

    fun onAddQuantity() {
        if (numberQuantity.value!! < maxQuantity) {
            extraDoneModel.value?.quantity = numberQuantity.value?.plus(1)!!;
            extraDoneModel.notifyValueChange();
        }
    }

    fun onRemoveQuantity() {
        if (numberQuantity.value!! > 0) {
            extraDoneModel.value?.quantity = numberQuantity.value?.minus(1)!!;
            extraDoneModel.notifyValueChange();
        }
    }


    fun onAddCart() {
        /*uiCallback?.onAddCart(extraDoneModel.value!!);*/
    }

    fun onBack() {
        uiCallback?.onBack();
    }

}