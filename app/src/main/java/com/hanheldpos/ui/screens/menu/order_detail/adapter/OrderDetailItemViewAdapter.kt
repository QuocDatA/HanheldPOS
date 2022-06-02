package com.hanheldpos.ui.screens.menu.order_detail.adapter

import android.annotation.SuppressLint
import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemProductOrderDetailViewBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class OrderDetailItemViewAdapter : BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_order_detail_view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductChosen>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemProductOrderDetailViewBinding
        when (ProductType.fromInt(item.ProductTypeId)) {
            ProductType.BUNDLE -> {
                val adapter = OrderDetailItemGroupAdapter(ProductType.BUNDLE)
                binding.productGroupRecyclerView.adapter = adapter
                adapter.submitList(item.groupProducts())
                adapter.notifyDataSetChanged()
            }
            ProductType.BUYX_GETY_DISC -> {}
            else -> {}
        }
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