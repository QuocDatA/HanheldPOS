package com.hanheldpos.ui.screens.menu.customers.customer_detail

import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.BaseUserView

interface CustomerDetailUV : BaseUserView {
    fun onFragmentBackPressed()
    fun onLoadedActivities(activities : List<OrderSummaryPrimary>?, isSuccess : Boolean,keyRequest: Int)
}