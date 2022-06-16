package com.hanheldpos.ui.screens.menu.order_detail

import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.ui.base.BaseUserView

interface OrderDetailViewUV : BaseUserView {
    fun onShowOrderDetail(order :OrderModel)
    fun onFragmentBackPressed()
}