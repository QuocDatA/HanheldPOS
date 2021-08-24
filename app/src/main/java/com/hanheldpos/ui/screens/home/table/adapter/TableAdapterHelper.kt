package com.hanheldpos.ui.screens.home.table.adapter


import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.home.table.TableModeViewType


class TableAdapterHelper(private val callback: AdapterCallBack) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<FloorTableItem>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<FloorTableItem?> = mutableListOf();

    fun submitList(list: MutableList<FloorTableItem?>) {
        this.list = list;
        currentIndex = 1;
        split(currentIndex);
    }

    fun next() {
        if ((currentIndex * maxItemViewProduct) < list.size) {
            currentIndex = currentIndex.plus(1);
            split(currentIndex);
        }

    }

    fun previous() {
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            split(currentIndex);
        }

    }

    private fun split(pagePosition: Int) {
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

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 1 : Enable
            * 2 : Disable
            * */
            FloorTableItem().apply {
                uiType = TableModeViewType.PrevButton.apply {
                    pos = if (currentIndex > 1) {
                        1
                    } else 2
                };
            },
            FloorTableItem().apply {
                uiType = TableModeViewType.NextButton.apply {
                    pos = if ((currentIndex * maxItemViewProduct) < list.size) {
                        1
                    } else 2
                };
            } )
        )
        callback.onListSplitCallBack(rs.toList());
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 13;
    }
}