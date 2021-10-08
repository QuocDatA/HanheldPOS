package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    val menus = MutableLiveData<MutableList<OrderMenuItemModel?>>(mutableListOf());
    val selectedMenu = MutableLiveData<OrderMenuItemModel>();

    fun initData() {
        OrderMenuDataMapper.menuDB = DataHelper.orderMenuResp!!;
    }

    fun onMenuChange(position: Int){
        menus.value = OrderMenuDataMapper.getMenuByBranch(position).toMutableList();
        // Init First Page
        selectedMenu.value = menus.value?.first();
    }

    fun getProductByMenu(menuItem: OrderMenuItemModel): List<ProductOrderItem?>? {
        return menuItem.childList;
    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}