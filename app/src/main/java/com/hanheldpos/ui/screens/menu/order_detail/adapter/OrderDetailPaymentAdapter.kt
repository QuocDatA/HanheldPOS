package com.hanheldpos.ui.screens.menu.order_detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemPaymentOrderDetailViewBinding
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class OrderDetailPaymentAdapter : BaseBindingListAdapter<PaymentOrder>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_order_detail_view
    }

    private class DiffCallBack : DiffUtil.ItemCallback<PaymentOrder>() {
        override fun areItemsTheSame(oldItem: PaymentOrder, newItem: PaymentOrder): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PaymentOrder, newItem: PaymentOrder): Boolean {
            return oldItem == newItem
        }

    }
}