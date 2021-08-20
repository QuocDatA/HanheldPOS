package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.data.api.pojo.order.CategoryItem
import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.model.home.order.category.CategoryModeViewType
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class OrderCategoryAdapterHelper(private val callBack : AdapterCallBack) {

    private var currentIndex : Int = 1;
    private var list: MutableList<CategoryItem?> = mutableListOf();

    fun submitList(list: MutableList<CategoryItem?>){
        this.list = list;
        currentIndex = 1;
        split(currentIndex);
    }

    fun next(){
        if ((currentIndex!! * maxItemViewCate) < list.size ){
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
        val rs: MutableList<CategoryItem> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewCate;
                val end: Int =
                    if (it.size > maxItemViewCate * pagePosition) maxItemViewCate * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<CategoryItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewCate) {
            rs.add(CategoryItem().apply {
                uiType = CategoryModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            CategoryItem().apply {
                uiType = CategoryModeViewType.DirectionButton;
            }
        ));

        val sub1 = rs.subList(0,itemPerCol);
        val sub2 = rs.subList(itemPerCol,rs.size);

        val lastrs: MutableList<CategoryItem> = mutableListOf();

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
        fun onListSplitCallBack(list: List<CategoryItem>)
    }


    companion object {
        @JvmStatic
        val itemPerCol: Int = 9;

        @JvmStatic
        val maxItemViewCate: Int = itemPerCol*2-1;
    }

}