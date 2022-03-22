package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.order.menu.MenuDataMapper
import com.hanheldpos.model.home.order.menu.OrderMenuItem
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    val menus = MutableLiveData<MutableList<OrderMenuItem?>>(mutableListOf());
    val selectedMenu = MutableLiveData<OrderMenuItem?>();

    fun initData() {
    }

    fun onMenuChange(position: Int){
        menus.value = MenuDataMapper.getMenuByBranch(position,DataHelper.menuLocalStorage!!).toMutableList();
        // Init First Page
        selectedMenu.postValue(menus.value?.firstOrNull())
    }

    fun getProductByMenu(menuItem: OrderMenuItem?): List<ProductMenuItem>? {
        return menuItem?.childList;
    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}