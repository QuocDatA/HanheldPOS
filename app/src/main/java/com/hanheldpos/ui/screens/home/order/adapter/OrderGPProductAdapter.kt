package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.databinding.ItemOrderProductGroupBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderGPProductAdapter (
    private val itemClickListener: BaseItemClickListener<ProductItem>
        ) : BaseBindingListAdapter<CategoryItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product_group;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CategoryItem>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        (holder.binding as ItemOrderProductGroupBinding).orderProductList.apply {
            adapter = OrderProductAdapter(
                itemClickListener
            ).apply {
                submitList(item.productList);

            };
        }

    }

    private class DiffCallBack : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

    }
}