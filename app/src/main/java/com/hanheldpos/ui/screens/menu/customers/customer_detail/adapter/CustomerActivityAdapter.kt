package com.hanheldpos.ui.screens.menu.customers.customer_detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCustomerActivityBinding
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.utils.DateTimeUtils

class CustomerActivityAdapter(private val listener: BaseItemClickListener<OrderSummaryPrimary>) :
    BaseBindingListAdapter<OrderSummaryPrimary>(DiffCallBack(), listener) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_customer_activity
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderSummaryPrimary>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemCustomerActivityBinding
        binding.name.text = item.OrderDescription
        binding.time.text = DateTimeUtils.strToStr(
            item.OrderCreateDate,
            DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS,
            DateTimeUtils.Format.DD_MM_YYYY_HH_MM_AA
        )
    }

    class DiffCallBack : DiffUtil.ItemCallback<OrderSummaryPrimary>() {
        override fun areItemsTheSame(
            oldItem: OrderSummaryPrimary,
            newItem: OrderSummaryPrimary
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: OrderSummaryPrimary,
            newItem: OrderSummaryPrimary
        ): Boolean {
            return oldItem == newItem
        }

    }
}