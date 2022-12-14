package com.hanheldpos.model.menu.orders

import com.hanheldpos.model.order.OrderSummaryPrimary

data class OrderMenuGroupItem(
    val createDate : String,
    val orders : List<OrderSummaryPrimary>,
    var isCollapse : Boolean = false,
)
