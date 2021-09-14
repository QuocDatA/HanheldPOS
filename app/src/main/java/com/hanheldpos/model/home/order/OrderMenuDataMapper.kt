package com.hanheldpos.model.home.order

import com.hanheldpos.data.api.pojo.order.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.getHierarchyList
import com.hanheldpos.data.api.pojo.order.getMenuList

object OrderMenuDataMapper {
    lateinit var orderMenuResp: OrderMenuResp
    //region ### Get ProductItem by ListToHierarchyItem

    /*fun getLevel_1(menuIndex: Int): List<OrderMenuItemModel> {
        *//*val rs = mutableListOf<OrderMenuItemModel>()

        orderMenuResp.getHierarchyList()?.forEach {

            it?.let {
                val menuIdInMenuList = orderMenuResp.getMenuList()?.get(menuIndex)?.id
                if (it.menusGuid == menuIdInMenuList) {

                    val orderMenuItemList = getOrderMenuItemList(it, null)

                    if (!orderMenuItemList.isNullOrEmpty()) {
                        rs.addAll(orderMenuItemList)
                    }
                }
            }
        }
        getChildListAsync(rs)
        return rs*//*
    }*/

    //endregion
}