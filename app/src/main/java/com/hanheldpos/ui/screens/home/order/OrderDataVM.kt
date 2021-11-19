package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.home.order.menu.OrderMenuItem
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    val menus = MutableLiveData<MutableList<OrderMenuItem?>>(mutableListOf());
    val selectedMenu = MutableLiveData<OrderMenuItem?>();

    fun initData() {
        OrderMenuDataMapper.menuDB = DataHelper.orderMenuResp!!;
    }

    fun onMenuChange(position: Int){
        menus.value = OrderMenuDataMapper.getMenuByBranch(position).toMutableList();
        // Init First Page
        selectedMenu.value = menus.value?.firstOrNull();
    }

    fun getProductByMenu(menuItem: OrderMenuItem?): List<ProductMenuItem>? {
        return menuItem?.childList;
    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}