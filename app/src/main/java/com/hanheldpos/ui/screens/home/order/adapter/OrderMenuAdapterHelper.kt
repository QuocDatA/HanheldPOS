package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItem

class OrderMenuAdapterHelper(private val callBack : AdapterCallBack) {

    private var currentIndex : Int = 1;
    private var list: MutableList<OrderMenuItem?> = mutableListOf();
    private var listOfOrderMenuPage: MutableList<List<OrderMenuItem?>> = mutableListOf()

    fun submitList(list: MutableList<OrderMenuItem?>){
        this.list = list;
        currentIndex = 1;
        listOfOrderMenuPage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex: Int = 1

        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                var tablePage = split(currentListIndex)
                listOfOrderMenuPage.add(tablePage)
                sizeOfMainList -= (tablePage.size - 2) // reduce list size except for 2 dirButton
                currentListIndex++
            }
        } else if (sizeOfMainList == 0) {
            var tempList = split(currentListIndex)
            listOfOrderMenuPage.add(tempList)
        }
        callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex-1])
    }

    fun next(){
        if ((currentIndex * maxItemViewCate) < list.size ){
            currentIndex = currentIndex.plus(1);
            callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex-1])
        }

    }

    fun previous(){
        if (currentIndex > 1){
            currentIndex = currentIndex.minus(1);
            callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex-1])
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

        var menuItemType = MenuModeViewType.DirectionDisableUpDown;
        if ((pagePosition * maxItemViewCate) < list.size ){
            menuItemType = MenuModeViewType.DirectionEnableDown;
        }
        if (pagePosition > 1){
            menuItemType = when(menuItemType){
                MenuModeViewType.DirectionEnableDown->MenuModeViewType.DirectionEnableUpDown
                MenuModeViewType.DirectionDisableUpDown->MenuModeViewType.DirectionEnableUp
                else -> MenuModeViewType.DirectionDisableUpDown
            };
        }

        // Add Direction Button
        rs.add(
            OrderMenuItem().apply {
                uiType = menuItemType

            }
        );

        val sub1 = rs.subList(0,itemPerCol);
        val sub2 = rs.subList(itemPerCol,rs.size);

        val lastrs: MutableList<OrderMenuItem?> = mutableListOf();

        for (i in 0..itemPerCol.minus(1)){
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
        val itemPerCol: Int = 9;

        @JvmStatic
        val maxItemViewCate: Int = itemPerCol*2-1;
    }

}