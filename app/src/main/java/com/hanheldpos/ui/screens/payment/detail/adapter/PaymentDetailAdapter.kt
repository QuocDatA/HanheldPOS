package com.hanheldpos.ui.screens.payment.detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.Voucher
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class PaymentDetailAdapter(
) : BaseBindingListAdapter<PaymentOrder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_detail
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaymentOrder>() {

        override fun areItemsTheSame(
            oldItem: PaymentOrder,
            newItem: PaymentOrder,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PaymentOrder,
            newItem: PaymentOrder
        ): Boolean {
            return false
        }

    }
}