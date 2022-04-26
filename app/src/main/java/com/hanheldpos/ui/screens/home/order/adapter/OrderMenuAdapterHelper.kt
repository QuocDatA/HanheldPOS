package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItem

class OrderMenuAdapterHelper( private val callBack: AdapterCallBack) {

    private var currentIndex: Int = 1
    private var list: MutableList<OrderMenuItem?> = mutableListOf();
    private var listOfMenuCategoryPage: MutableList<List<OrderMenuItem?>> = mutableListOf();
    private val maxItemViewCate: Int = itemPerCol * 2 - 1;

    fun submitList(list: MutableList<OrderMenuItem?>) {
        this.list.clear()
        this.list = list
        currentIndex = 1
        listOfMenuCategoryPage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex = 1 // page currentIndex

        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                val menuCategoryPage = split(currentListIndex)
                listOfMenuCategoryPage.add(menuCategoryPage)
                sizeOfMainList -= (menuCategoryPage.size - 1) // reduce list size except for 2 dirButton
                currentListIndex++
            }
        } else if (sizeOfMainList == 0) {
            val tempList = split(currentListIndex)
            listOfMenuCategoryPage.add(tempList)
        }

        callBack.onListSplitCallBack(listOfMenuCategoryPage[currentIndex - 1])
    }

    fun next() {
        if ((currentIndex * maxItemViewCate) < list.size) {
            currentIndex = currentIndex.plus(1);
            callBack.onListSplitCallBack(listOfMenuCategoryPage[currentIndex - 1])
        }

    }

    fun previous() {
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            callBack.onListSplitCallBack(listOfMenuCategoryPage[currentIndex - 1])
        }

    }

    private fun split(pagePosition: Int): MutableList<OrderMenuItem?> {
        val rs: MutableList<OrderMenuItem> = mutableListOf();

        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewCate;
                val end: Int =
                    if (it.size > maxItemViewCate * pagePosition) maxItemViewCate * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<OrderMenuItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewCate) {
            rs.add(OrderMenuItem().apply {
                uiType = MenuModeViewType.Empty
            })
        }

        // set state for Direction Button to add to list
        val dirBtn: MenuModeViewType = if (list.size <= maxItemViewCate) {
            MenuModeViewType.NoneButtonEnable
        } else if (pagePosition <= 1) {
            MenuModeViewType.NextButtonEnable
        } else {
            if ((pagePosition * maxItemViewCate) < list.size && pagePosition > 1) {
                MenuModeViewType.BothButtonEnable
            } else {
                MenuModeViewType.PrevButtonEnable
            }
        }

        rs.add(OrderMenuItem().apply { uiType = dirBtn })

        val sub1 = rs.subList(0, itemPerCol);
        val sub2 = rs.subList(itemPerCol, rs.size);

        val lastrs: MutableList<OrderMenuItem?> = mutableListOf();

        for (i in 0..itemPerCol.minus(1)) {
            lastrs.addAll(
                mutableListOf(
                    sub1[i],
                    sub2[i],
                )
            )
        }
        return lastrs
    }


    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<OrderMenuItem?>)
    }

    companion object {
        @JvmStatic
        val itemPerCol: Int = 10;
    }
}