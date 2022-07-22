package com.hanheldpos.ui.screens.menu.orders.unsync

import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.DateTimeUtils

class UnsyncOrdersVM : BaseUiViewModel<UnsyncOrdersUV>() {


    fun groupUnsyncOrders(orders: List<OrderCompletedEntity>): List<OrderMenuGroupItem> {
        orders.filter { OrderHelper.isValidOrderPush(it) }.let { listNeedPush ->
            return listNeedPush.map { item -> DatabaseMapper.mappingOrderReqFromEntity(item).OrderSummary }
                .groupBy { item ->
                    DateTimeUtils.strToStr(
                        item.OrderCreateDate,
                        DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS,
                        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
                    )
                }.map {
                    OrderMenuGroupItem(
                        createDate = it.key,
                        orders = it.value
                    )
                }
        }
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}