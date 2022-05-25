package com.hanheldpos.ui.screens.menu.orders

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.model.menu.orders.OrdersOptionType
import com.hanheldpos.model.menu.report.ReportOptionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav

class OrdersMenuVM : BaseUiViewModel<OrdersMenuUV>() {

    fun initOrdersMenuList(context : Context) : List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                type = OrdersOptionType.UNSYNC,
                name = getNameMenu(OrdersOptionType.UNSYNC, context)
            ),
            ItemOptionNav(
                type = OrdersOptionType.SYNCED,
                name = getNameMenu(OrdersOptionType.SYNCED, context)
            ),
        )
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    private fun getNameMenu(type : OrdersOptionType, context: Context): String {
        return when(type){
            OrdersOptionType.UNSYNC -> context.getString(R.string.unsync)
            OrdersOptionType.SYNCED -> context.getString(R.string.synced)
        }
    }
}