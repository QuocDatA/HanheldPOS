package com.hanheldpos.ui.screens.menu

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.model.menu_nav_opt.ItemMenuOptionNav
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class MenuVM : BaseUiViewModel<MenuUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun initMenuItemList(context: Context): List<ItemMenuOptionNav> {
        return mutableListOf(
            ItemMenuOptionNav(
                type = NavBarOptionType.ORDERS,
                name = getNameMenu(NavBarOptionType.ORDERS, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.TRANSACTIONS,
                name = getNameMenu(NavBarOptionType.TRANSACTIONS, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.REPORTS,
                name = getNameMenu(NavBarOptionType.REPORTS, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.CUSTOMER,
                name = getNameMenu(NavBarOptionType.CUSTOMER, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.ORDER_HISTORY,
                name = getNameMenu(NavBarOptionType.ORDER_HISTORY, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.SETTINGS,
                name = getNameMenu(NavBarOptionType.SETTINGS, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.SUPPORT,
                name = getNameMenu(NavBarOptionType.SUPPORT, context)
            ),
            ItemMenuOptionNav(
                type = NavBarOptionType.LOGOUT_USER,
                name = getNameMenu(NavBarOptionType.LOGOUT_USER, context)
            ),
            ItemMenuOptionNav(
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
            NavBarOptionType.LOGOUT_USER -> context.getString(R.string.logout_user)
            NavBarOptionType.RESET_SYSTEM -> context.getString(R.string.reset_system)
        }
    }
}