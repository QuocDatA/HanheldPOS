package com.hanheldpos.ui.screens.menu

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.model.menu.NavBarOptionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class MenuVM : BaseUiViewModel<MenuUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun initMenuItemList(context: Context): List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                type = NavBarOptionType.ORDERS,
                name = getNameMenu(NavBarOptionType.ORDERS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.TRANSACTIONS,
                name = getNameMenu(NavBarOptionType.TRANSACTIONS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.REPORTS,
                name = getNameMenu(NavBarOptionType.REPORTS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.CUSTOMER,
                name = getNameMenu(NavBarOptionType.CUSTOMER, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.ORDER_HISTORY,
                name = getNameMenu(NavBarOptionType.ORDER_HISTORY, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.SETTINGS,
                name = getNameMenu(NavBarOptionType.SETTINGS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.SUPPORT,
                name = getNameMenu(NavBarOptionType.SUPPORT, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.LOGOUT_DEVICE,
                name = getNameMenu(NavBarOptionType.LOGOUT_DEVICE, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.RESET_SYSTEM,
                name = getNameMenu(NavBarOptionType.RESET_SYSTEM, context)
            ),
        )
    }

    private fun getNameMenu(type : NavBarOptionType, context: Context): String {
        return when(type){
            NavBarOptionType.ORDERS -> context.getString(R.string.orders)
            NavBarOptionType.TRANSACTIONS -> context.getString(R.string.transactions)
            NavBarOptionType.REPORTS -> context.getString(R.string.reports)
            NavBarOptionType.CUSTOMER -> context.getString(R.string.customer)
            NavBarOptionType.ORDER_HISTORY -> context.getString(R.string.order_history)
            NavBarOptionType.SETTINGS -> context.getString(R.string.settings)
            NavBarOptionType.SUPPORT -> context.getString(R.string.support)
            NavBarOptionType.LOGOUT_DEVICE -> context.getString(R.string.logout_device)
            NavBarOptionType.RESET_SYSTEM -> context.getString(R.string.reset_system)
        }
    }
}