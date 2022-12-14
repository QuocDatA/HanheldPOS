package com.hanheldpos.ui.screens.payment.voucher.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.Voucher
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CashVoucherAdapter(private val onCashVoucherClickListener: BaseItemClickListener<Voucher>) :
    BaseBindingListAdapter<Voucher>(
        DiffCallback(),
        itemClickListener = onCashVoucherClickListener
    ) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cash_voucher
    }

    private class DiffCallback : DiffUtil.ItemCallback<Voucher>() {
        override fun areItemsTheSame(
            oldItem: Voucher,
            newItem: Voucher,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Voucher,
            newItem: Voucher
        ): Boolean {
            return false
        }

    }
}