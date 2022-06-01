package com.hanheldpos.ui.screens.menu.order_history

import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.BaseUserView

interface OrderHistoryUV : BaseUserView {
    fun onFragmentBackPressed()
    fun onLoadOrderHistory(orders : List<OrderMenuGroupItem>?)
}