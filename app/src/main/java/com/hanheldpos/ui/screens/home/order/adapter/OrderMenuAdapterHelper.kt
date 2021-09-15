package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel

class OrderMenuAdapterHelper(private val callBack : AdapterCallBack) {

    private var currentIndex : Int = 1;
    private var list: MutableList<OrderMenuItemModel?> = mutableListOf();

    fun submitList(list: MutableList<OrderMenuItemModel?>){
        this.list = list;
        currentIndex = 1;
        split(currentIndex);
    }

    fun next(){
        if ((currentIndex * maxItemViewCate) < list.size ){
            currentIndex = currentIndex.plus(1);
            split(currentIndex);
        }

    }

    fun previous(){
        if (currentIndex > 1){
            currentIndex = currentIndex.minus(1);
            split(currentIndex);
        }

    }

    private fun split(pagePosition: Int){
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
                uiType = MenuModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 0 : Disable All
            * 1 : Enable Next
            * 2 : Enable Previous
            * 3 : Enable All
            * */
            OrderMenuItemModel().apply {
                uiType = MenuModeViewType.DirectionButton.apply {
                    var ss = 0;
                    if ((currentIndex * maxItemViewCate) < list.size ){
                        ss = 1;
                    }
                    if (currentIndex > 1){
                        ss = when(ss){
                            1->3
                            0->2
                            else -> 0
                        };
                    }
                    pos = ss;
                };
            }
        ));

        val sub1 = rs.subList(0,itemPerCol);
        val sub2 = rs.subList(itemPerCol,rs.size);

        val lastrs: MutableList<OrderMenuItemModel> = mutableListOf();

        for (i in 0..itemPerCol.minus(1)){
            lastrs.addAll(
                mutableListOf(
                    sub1[i],
                    sub2[i],
                )
            )
        }
        callBack.onListSplitCallBack(lastrs.toList());
    }


    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<OrderMenuItemModel>)
    }


    companion object {
        @JvmStatic
        val itemPerCol: Int = 9;

        @JvmStatic
        val maxItemViewCate: Int = itemPerCol*2-1;
    }

}