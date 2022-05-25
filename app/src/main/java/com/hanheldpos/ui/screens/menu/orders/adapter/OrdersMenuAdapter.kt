package com.hanheldpos.ui.screens.menu.orders.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.binding.setBackColor
import com.hanheldpos.databinding.ItemOrdersMenuBinding
import com.hanheldpos.databinding.ItemOrdersMenuBindingImpl
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrdersMenuAdapter(listener: BaseItemClickListener<OrderSummaryPrimary>) :
    BaseBindingListAdapter<OrderSummaryPrimary>(DiffCallBack(), listener) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_orders_menu
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderSummaryPrimary>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemOrdersMenuBinding
        binding.paymentStatusText.text =
            DataHelper.orderStatusLocalStorage?.Payments?.findTextPaymentStatus(
                item?.PaymentStatusId ?: 0
            )
        setBackColor(
            binding.paymentStatusColor,
            DataHelper.orderStatusLocalStorage?.Payments?.findColorPaymentStatus(
                item?.PaymentStatusId ?: 0
            )
        )
        binding.orderStatusText.text =
            DataHelper.orderStatusLocalStorage?.Fullfillments?.findTextOrderStatus(
                item?.DiningOptionId ?: 0, item?.OrderStatusId ?: 0
            )
        setBackColor(
            binding.orderStatusColor,
            DataHelper.orderStatusLocalStorage?.Fullfillments?.findTextOrderStatus(
                item?.DiningOptionId ?: 0, item?.OrderStatusId ?: 0
            )
        )
    }

    private class DiffCallBack : DiffUtil.ItemCallback<OrderSummaryPrimary>() {
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