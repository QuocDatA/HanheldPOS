package com.hanheldpos.data.api.pojo.customer

import com.hanheldpos.model.order.OrderSummaryPrimary

data class CustomerActivitiesResp(
    val PageNo : Int?,
    val PageSize :Int?,
    val TotalPage : Int?,
    val OrderList : List<OrderSummaryPrimary>?,
)
