package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    private var orderMenuResp: OrderMenuResp? = null;
    val orderMenuLevel1 = MutableLiveData<MutableList<OrderMenuItemModel?>>(mutableListOf());
    val orderMenuLevel1Selected = MutableLiveData<OrderMenuItemModel>();
    val cartModelLD = MutableLiveData<CartModel>();

    fun initData() {
        OrderMenuDataMapper.orderMenuResp = DataHelper.orderMenuResp!!;
        orderMenuResp = OrderMenuDataMapper.orderMenuResp;
    }

    fun onMenuChange(position: Int){
        orderMenuLevel1.value = OrderMenuDataMapper.getLevel_1(position).toMutableList();
        // Init First Page
        orderMenuLevel1Selected.value = orderMenuLevel1.value?.first();
    }

    fun getProductByMenu(menuItem: OrderMenuItemModel): List<ProductOrderItem?>? {
        /*return OrderMenuDataMapper.getLevel_2(categoryGuid = menuItem.id);*/
        return menuItem.childList;
    }

    // Cart
    fun addProductCompleteToCart(item: OrderItemModel) {

    }

    fun deleteAllProductCart() {

    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}