package com.hanheldpos.ui.screens.menu.orders.synced

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class SyncedOrdersVM : BaseUiViewModel<SyncedOrdersUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun showFilter() {
        uiCallback?.onShowFilter()
    }
}