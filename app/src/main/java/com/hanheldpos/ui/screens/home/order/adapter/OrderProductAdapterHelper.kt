package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.product.ProductOrderItem

class OrderProductAdapterHelper(
    private val callBack: AdapterCallBack
) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<ProductOrderItem>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<ProductOrderItem?> = mutableListOf();

    fun submitList(list: MutableList<ProductOrderItem?>) {
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
        val rs: MutableList<ProductOrderItem> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<ProductOrderItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewProduct) {
            rs.add(ProductOrderItem().apply {
                uiType = ProductModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 1 : Enable
            * 2 : Disable
            * */
            ProductOrderItem().apply {
                uiType = ProductModeViewType.PrevButton.apply {
                    pos = if (currentIndex > 1) {
                        1
                    } else 2
                };
            },
            ProductOrderItem().apply {
                uiType = ProductModeViewType.NextButton.apply {
                    pos = if ((currentIndex * maxItemViewProduct) < list.size) {
                        1
                    } else 2
                };
            } )
        )
        callBack.onListSplitCallBack(rs.toList());
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 22;
    }
}