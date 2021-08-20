package com.hanheldpos.ui.screens.home.table.adapter

import com.hanheldpos.data.api.pojo.table.FloorTableItem

class TableAdapterHelper(private val calback : AdapterCallback) {



    interface AdapterCallback {
        fun onSplitListCallback(list: List<FloorTableItem>)
    }
}