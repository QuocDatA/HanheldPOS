package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.model.home.ProductModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderProductAdapter(
    listener: BaseItemClickListener<ProductModel>
) : BaseBindingListAdapter<ProductModel>(DiffCallback(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product;
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }

    }
}