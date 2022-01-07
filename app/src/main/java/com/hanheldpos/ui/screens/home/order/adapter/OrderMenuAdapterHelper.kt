package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItem

class OrderMenuAdapterHelper(private val callBack : AdapterCallBack) {

    private var list: MutableList<OrderMenuItem?> = mutableListOf();

    fun submitList(list: MutableList<OrderMenuItem?>){
        this.list.clear()
        this.list = list;
        this.list = split(1)
        callBack.onListSplitCallBack(this.list)
    }

    private fun split(pagePosition: Int): MutableList<OrderMenuItem?> {
        val rs: MutableList<OrderMenuItem> = mutableListOf();

        val itemPerCol: Int = list.size / 2;
        val maxItemViewCate: Int = itemPerCol*2;

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


//    companion object {
//        @JvmStatic
//        val itemPerCol: Int = 9;
//
//        @JvmStatic
//        val maxItemViewCate: Int = itemPerCol*2-1;
//    }

}