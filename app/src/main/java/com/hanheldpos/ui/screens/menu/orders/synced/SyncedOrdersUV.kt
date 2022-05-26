package com.hanheldpos.ui.screens.menu.orders.synced

import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.BaseUserView

interface SyncedOrdersUV : BaseUserView {
    fun onFragmentBackPressed()
    fun onLoadOrderFilter(orders : List<OrderSummaryPrimary>?)
    fun onShowFilter()
}