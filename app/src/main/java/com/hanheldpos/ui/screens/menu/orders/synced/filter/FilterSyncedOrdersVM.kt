package com.hanheldpos.ui.screens.menu.orders.synced.filter

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class FilterSyncedOrdersVM : BaseUiViewModel<FilterSyncedOrdersUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}