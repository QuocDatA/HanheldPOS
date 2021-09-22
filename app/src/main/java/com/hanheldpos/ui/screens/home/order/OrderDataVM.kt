package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.*
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper.getChildList
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import java.text.FieldPosition

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    private var orderMenuResp: OrderMenuResp? = null;
    val orderMenuLevel1 = MutableLiveData<MutableList<OrderMenuItemModel?>>(mutableListOf());
    val orderMenuLevel1Selected = MutableLiveData<OrderMenuItemModel>();

    val productInCartLD = MutableLiveData<MutableList<ProductCompleteModel>?>(mutableListOf())
    val productQuantityInCartLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumOf {
            it.quantity
        } ?: 0
    }
    val productTotalPriceLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumOf {
            it.getPriceTotal()
        } ?: 0.0
    }

    fun initData() {
        OrderMenuDataMapper.orderMenuResp = DataHelper.orderMenuResp!!;
        orderMenuResp = OrderMenuDataMapper.orderMenuResp;
    }

    fun onMenuChange(position: Int){
        orderMenuLevel1.value = OrderMenuDataMapper.getLevel_1(position).toMutableList();
        // Init First Page
        orderMenuLevel1Selected.value = orderMenuLevel1.value?.first();
    }



    fun getOrderMenu(): OrderMenuResp? {
        return this.orderMenuResp;
    }
    fun getProductByMenu(menuItem: OrderMenuItemModel): List<ProductOrderItem?>? {
        /*return OrderMenuDataMapper.getLevel_2(categoryGuid = menuItem.id);*/
        return menuItem.childList;
    }

    // Cart
    fun addProductCompleteToCart(item: ProductCompleteModel) {
        productInCartLD.value?.add(item);
        productInCartLD.notifyValueChange();
    }

    fun deleteAllProductCart() {
        productInCartLD.value?.clear();
        productInCartLD.notifyValueChange();
    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}