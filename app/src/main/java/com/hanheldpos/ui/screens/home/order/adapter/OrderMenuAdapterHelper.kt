package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel

class OrderMenuAdapterHelper(private val callBack : AdapterCallBack) {

    private var currentIndex : Int = 1;
    private var list: MutableList<OrderMenuItemModel?> = mutableListOf();
    private var listOfOrderMenuPage: MutableList<List<OrderMenuItemModel?>> = mutableListOf();

    fun submitList(list: MutableList<OrderMenuItemModel?>) {
        this.list = list
        listOfOrderMenuPage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex: Int = 1

        currentIndex = 1
        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                var tempList = split(currentListIndex)
                listOfOrderMenuPage.add(tempList)
                sizeOfMainList -= (tempList.size-1) // reduce the dirButton
                currentListIndex++
            }
            callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex - 1])
        }

    }

    fun next(){
        if ((currentIndex * maxItemViewCate) < list.size ) {
            currentIndex = currentIndex.plus(1);
            callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex - 1])
        }

    }

    fun previous(){
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            callBack.onListSplitCallBack(listOfOrderMenuPage[currentIndex - 1])
        }

    }

    private fun split(pagePosition: Int): MutableList<OrderMenuItemModel?> {
        val rs: MutableList<OrderMenuItemModel> = mutableListOf();

        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewCate;
                val end: Int =
                    if (it.size > maxItemViewCate * pagePosition) maxItemViewCate * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<OrderMenuItemModel>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewCate) {
            rs.add(OrderMenuItemModel().apply {
                uiType = MenuModeViewType.Empty
            })
        }

        // set state for Direction Button to add to list
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
            OrderMenuItemModel().apply {
                uiType = menuItemType

            }
        )

        val sub1 = rs.subList(0,itemPerCol);
        val sub2 = rs.subList(itemPerCol,rs.size);

        val lastrs: MutableList<OrderMenuItemModel?> = mutableListOf();

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
        fun onListSplitCallBack(list: List<OrderMenuItemModel?>)
    }


    companion object {
        @JvmStatic
        val itemPerCol: Int = 9;

        @JvmStatic
        val maxItemViewCate: Int = itemPerCol*2-1;
    }

}