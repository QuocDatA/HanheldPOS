package com.hanheldpos.ui.screens.menu.order_detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter

class OrderDetailItemGroupBundleAdapter : BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_group_combo_detail_view
    }

    class DiffCallBack : DiffUtil.ItemCallback<ProductChosen>() {
        override fun areItemsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

    }
}