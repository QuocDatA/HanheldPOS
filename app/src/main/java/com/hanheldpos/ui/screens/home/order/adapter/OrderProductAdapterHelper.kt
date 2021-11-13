package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.model.home.order.ProductModelViewType
import com.hanheldpos.model.product.ProductOrderItem

class OrderProductAdapterHelper(
    private val callBack: AdapterCallBack
) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<ProductOrderItem?>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<ProductOrderItem?> = mutableListOf();
    private var listOfProductPage: MutableList<List<ProductOrderItem?>> = mutableListOf();

    fun submitList(list: MutableList<ProductOrderItem?>) {
        this.list = list;
        currentIndex = 1;
        listOfProductPage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex: Int = 1 // page currentIndex

        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                var productPage = split(currentListIndex)
                listOfProductPage.add(productPage)
                sizeOfMainList -= (productPage.size-2) // reduce list size except for 2 dirButton
                currentListIndex++
            }
        }
        else if (sizeOfMainList == 0) {
            var tempList = split(currentListIndex)
            listOfProductPage.add(tempList)
        }

        callBack.onListSplitCallBack(listOfProductPage[currentIndex - 1])
    }

    fun next() {
        if ((currentIndex * maxItemViewProduct) < list.size) {
            currentIndex = currentIndex.plus(1);
            callBack.onListSplitCallBack(listOfProductPage[currentIndex - 1])
        }

    }

    fun previous() {
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            callBack.onListSplitCallBack(listOfProductPage[currentIndex - 1])
        }

    }

    private fun split(pagePosition: Int): MutableList<ProductOrderItem?> {
        val rs: MutableList<ProductOrderItem?> = mutableListOf();
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
                uiType = ProductModelViewType.Empty;
            })
        }

        // set state for Direction Button to add to list
        var nextButtonType = ProductModelViewType.NextButtonDisable
        var prevButtonType = ProductModelViewType.PrevButtonDisable
        if (pagePosition > 1) {
            prevButtonType = ProductModelViewType.PrevButtonEnable
        }
        if ((pagePosition * maxItemViewProduct) < list.size) {
            nextButtonType = ProductModelViewType.NextButtonEnable
        }

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 1 : Enable
            * 2 : Disable
            * */
            ProductOrderItem().apply {
                uiType = prevButtonType
            },
            ProductOrderItem().apply {
                uiType = nextButtonType
            } )
        )
        return rs
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 22;
    }
}