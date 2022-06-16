package com.hanheldpos.data.api.pojo.order.filter

import com.hanheldpos.model.order.OrderSummaryPrimary

data class OrderFilterResp(
    val PageNo : Int?,
    val PageSize :Int?,
    val TotalPage : Int?,
    val OrderList : List<OrderSummaryPrimary>?,
)
