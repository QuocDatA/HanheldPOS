package com.hanheldpos.ui.screens.menu.order_detail.adapter

import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class OrderDetailItemViewAdapter : BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_order_detail_view
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductChosen>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductChosen>() {
        override fun areItemsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

    }
}