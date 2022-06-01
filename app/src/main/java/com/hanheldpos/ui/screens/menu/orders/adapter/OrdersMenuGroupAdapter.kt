package com.hanheldpos.ui.screens.menu.orders.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrdersMenuGroupBinding
import com.hanheldpos.extension.addItemDecorationWithoutLastDivider
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.utils.DateTimeUtils

class OrdersMenuGroupAdapter(private val listener : BaseItemClickListener<OrderSummaryPrimary>) : BaseBindingListAdapter<OrderMenuGroupItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_orders_menu_group
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderMenuGroupItem>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemOrdersMenuGroupBinding
        binding.title.text = item.createDate

        binding.groupTitle.setOnClickListener {
            item.isCollapse = !item.isCollapse
            notifyItemChanged(position)
        }
        val adapter = OrdersMenuAdapter(listener = listener)
        binding.listOrders.apply {
            this.adapter = adapter
            addItemDecorationWithoutLastDivider()
        }
        adapter.submitList(item.orders)
        adapter.notifyDataSetChanged()
    }

    private class DiffCallBack : DiffUtil.ItemCallback<OrderMenuGroupItem>() {
        override fun areItemsTheSame(
            oldItem: OrderMenuGroupItem,
            newItem: OrderMenuGroupItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: OrderMenuGroupItem,
            newItem: OrderMenuGroupItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}