package com.hanheldpos.ui.screens.home.order.adapter

import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapterHelper

class OrderProductAdapterHelper(
    private val callBack: AdapterCallBack
) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<ProductMenuItem>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<ProductMenuItem?> = mutableListOf();

    fun submitList(list: MutableList<ProductMenuItem?>) {
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
        val rs: MutableList<ProductMenuItem> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<ProductMenuItem>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewProduct) {
            rs.add(ProductMenuItem().apply {
                uiType = ProductModeViewType.Empty;
            })
        }

        // set state for Direction Button to add to list
        var nextButtonType = ProductModeViewType.NextButtonDisable
        var prevButtonType = ProductModeViewType.PrevButtonDisable
        if (pagePosition > 1) {
            prevButtonType = ProductModeViewType.PrevButtonEnable
        }
        if ((pagePosition * maxItemViewProduct) < list.size) {
            nextButtonType = ProductModeViewType.NextButtonEnable
        }

        // Add Direction Button
        rs.addAll(listOf(
            /*
            * 1 : Enable
            * 2 : Disable
            * */
            ProductMenuItem().apply {
                uiType = prevButtonType
            },
            ProductMenuItem().apply {
                uiType = nextButtonType
            } )
        )
        callBack.onListSplitCallBack(rs.toList());
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 22;
    }
}