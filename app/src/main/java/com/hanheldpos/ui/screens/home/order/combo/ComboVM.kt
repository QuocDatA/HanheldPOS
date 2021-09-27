package com.hanheldpos.ui.screens.home.order.combo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.order.OrderItemModel
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
    val extraDoneItemComboList = MutableLiveData<MutableList<ExtraDoneModel>>(mutableListOf())

    /**
     *  List name of selected products in combo list
     */
    val selectedCombo = MutableLiveData<ComboEvent>()

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun initDefaultComboList(
        listProductComboItem: MutableList<ProductComboItem>
    ) {
        //Default requirements for selected product combo
        val comboGroupList: MutableList<ItemComboGroupManager?> = mutableListOf()
        listProductComboItem.map {
            comboGroupList.add(
                ItemComboGroupManager(
                    productComboItem = it,
                ).apply {
                    comboDetailAdapter = ComboItemAdapter(
                        modeViewType = ComboItemAdapter.ComboItemViewType.ForChoose,
                        listener = object : ComboItemAdapter.ComboItemListener {
                            override fun onComboItemChoose(
                                action: ComboItemActionType,
                                item: ComboPickedItemViewModel
                            ) {
                                uiCallback?.openProductDetail(requireQuantity(), item.copy());
                            }

                        }
                    ).apply {
                        (it.id?.let { it1 ->
                            this.submitList(extraDoneModel.value?.productOrderItem?.getComboList(
                                it1
                            )?.map { it2 ->
                                ComboPickedItemViewModel(
                                    comboParentId = it.comboGuid,
                                    selectedComboItem = it2
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

    fun onChooseItemComboSuccess(comboParent: String? ,item: ComboPickedItemViewModel) {
        selectedCombo.value?.data?.let {
            it.listItemsByGroup?.forEach { it1 ->
                if (it1?.productComboItem?.comboGuid == comboParent){
                    it1?.listSelectedComboItems?.let {
                        it.add(item)
                        (it1.comboItemSelectedAdapter as ComboItemAdapter).submitList(it);
                        (it1.comboItemSelectedAdapter as ComboItemAdapter).notifyDataSetChanged();
                    }
                    (it.comboAdapter as ComboGroupAdapter).notifyDataSetChanged()
                    return;
                }
            }
        }
    }

    fun onAddCart() {
        /*uiCallback?.onAddCart(extraDoneModel.value!!);*/
    }

    fun onBack() {
        uiCallback?.onBack();
    }

}