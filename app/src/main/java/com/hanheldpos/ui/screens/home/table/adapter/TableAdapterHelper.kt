package com.hanheldpos.ui.screens.home.table.adapter


import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.home.order.ProductModelViewType
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper


class TableAdapterHelper(private val callback: AdapterCallBack) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<FloorTableItem?>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<FloorTableItem?> = mutableListOf();
    private var listOfTablePage: MutableList<List<FloorTableItem?>> = mutableListOf();

    fun submitList(list: MutableList<FloorTableItem?>) {
        this.list = list;
        currentIndex = 1;
        listOfTablePage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex: Int = 1 // page currentIndex

        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                var tablePage = split(currentListIndex)
                listOfTablePage.add(tablePage)
                sizeOfMainList -= (tablePage.size-2) // reduce list size except for 2 dirButton
                currentListIndex++
            }
        }
        else if (sizeOfMainList == 0) {
            var tempList = split(currentListIndex)
            listOfTablePage.add(tempList)
        }

        callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
    }

    fun next() {
        if ((currentIndex * maxItemViewProduct) < list.size) {
            currentIndex = currentIndex.plus(1);
            callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
        }

    }

    fun previous() {
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
        }

    }

    private fun split(pagePosition: Int): MutableList<FloorTableItem> {
        val rs: MutableList<FloorTableItem> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<FloorTableItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewProduct) {
            rs.add(FloorTableItem().apply {
                uiType = TableModeViewType.Empty;
            })
        }

        // set state for Direction Button to add to list
        var nextButtonType = TableModeViewType.NextButtonDisable
        var prevButtonType = TableModeViewType.PrevButtonDisable
        if (pagePosition > 1) {
            prevButtonType = TableModeViewType.PrevButtonEnable
        }
        if ((pagePosition * maxItemViewProduct) < list.size) {
            nextButtonType = TableModeViewType.NextButtonEnable
        }

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 1 : Enable
            * 2 : Disable
            * */
            FloorTableItem().apply {
                uiType = prevButtonType
            },
            FloorTableItem().apply {
                uiType = nextButtonType
            } )
        )
        return rs
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 13;
    }
}