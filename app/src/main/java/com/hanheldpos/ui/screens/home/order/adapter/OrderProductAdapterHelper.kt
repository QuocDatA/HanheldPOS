package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.model.home.order.product.ProductModeViewType

class OrderProductAdapterHelper(
    private val callBack: AdapterCallBack
) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<ProductItem>)
    }

    private var currentIndex: Int = 1;

    private var list : MutableList<ProductItem?> = mutableListOf();

    fun submitList(list: MutableList<ProductItem?>){
        this.list = list;
        currentIndex = 1;
        split(currentIndex);
    }

    fun next() {
        if ((currentIndex!! * maxItemViewProduct) < list.size) {
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
        val rs: MutableList<ProductItem> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<ProductItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewProduct) {
            rs.add(ProductItem().apply {
                uiType = ProductModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            ProductItem().apply {
                uiType = ProductModeViewType.PrevButton;
            }, ProductItem().apply {
                uiType = ProductModeViewType.NextButton;
            })
        )
        callBack.onListSplitCallBack(rs.toList());
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 22;
    }
}