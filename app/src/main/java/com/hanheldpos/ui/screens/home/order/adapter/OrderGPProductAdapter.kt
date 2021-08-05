package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderProductGroupBinding
import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.model.home.order.ProductModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderGPProductAdapter (
    private val itemClickListener: BaseItemClickListener<ProductModel>
        ) : BaseBindingListAdapter<CategoryModel>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product_group;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CategoryModel>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        (holder.binding as ItemOrderProductGroupBinding).orderProductList.adapter = OrderProductAdapter(
            itemClickListener
        ).apply {
            submitList(item.childList);
        };

    }

    private class DiffCallBack : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem;
        }

    }
}