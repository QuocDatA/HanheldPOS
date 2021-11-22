package com.hanheldpos.ui.screens.menu

import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class MenuVM : BaseUiViewModel<MenuUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun initMenuItemList(): List<MenuFragment.FakeMenuItem> {
        val menuItemList = mutableListOf<MenuFragment.FakeMenuItem>(
            MenuFragment.FakeMenuItem(name = "Order"),
            MenuFragment.FakeMenuItem(name = "Transaction"),
            MenuFragment.FakeMenuItem(name = "Report"),
            MenuFragment.FakeMenuItem(name = "Customer"),
            MenuFragment.FakeMenuItem(name = "Items"),
            MenuFragment.FakeMenuItem(name = "Settings"),
            MenuFragment.FakeMenuItem(name = "Support"),
            MenuFragment.FakeMenuItem(name = "Logout User"),
            MenuFragment.FakeMenuItem(name = "Reset System"),
        )
        return menuItemList
    }
}