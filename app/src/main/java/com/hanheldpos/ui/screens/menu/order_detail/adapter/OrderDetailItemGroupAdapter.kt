package com.hanheldpos.ui.screens.menu.order_detail.adapter

import android.annotation.SuppressLint
import android.util.Pair
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemGroupChildOrderDetailViewBinding
import com.hanheldpos.databinding.ItemProductGroupComboDetailViewBinding
import com.hanheldpos.model.order.GroupProductChosen
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class OrderDetailItemGroupAdapter(val type: ProductType) :
    BaseBindingListAdapter<GroupProductChosen>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_buy_x_get_y_child_order_detail_view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<GroupProductChosen>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemGroupChildOrderDetailViewBinding
        when (type) {
            ProductType.BUNDLE -> {
                val adapter = OrderDetailItemGroupBundleAdapter()
                binding.groupChildOrderDetailItem.adapter = adapter
                adapter.submitList(item.productChosenList)
                adapter.notifyDataSetChanged()
            }

            ProductType.BUYX_GETY_DISC -> {

            }
            else -> {}
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<GroupProductChosen>() {
        override fun areItemsTheSame(
            oldItem: GroupProductChosen,
            newItem: GroupProductChosen
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GroupProductChosen,
            newItem: GroupProductChosen
        ): Boolean {
            return oldItem == newItem
        }

    }
}