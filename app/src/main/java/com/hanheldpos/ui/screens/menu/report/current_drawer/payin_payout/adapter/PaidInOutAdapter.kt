package com.hanheldpos.ui.screens.menu.report.current_drawer.payin_payout.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.databinding.ItemCashDrawerPaidInOutBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.PriceUtils

class PaidInOutAdapter : BaseBindingListAdapter<PaidInOutListResp>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cash_drawer_paid_in_out
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<PaidInOutListResp>, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ItemCashDrawerPaidInOutBinding

        binding.timeApply.text =
            DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    item.CreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
        binding.description.text = item.Description
        binding.amount.text =
            if (item.Payable ?: 0.0 > 0.0) "-${PriceUtils.formatStringPrice(item.Payable?.toInt().toString())}"
            else
                PriceUtils.formatStringPrice(item.Receivable?.toInt().toString())
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaidInOutListResp>() {
        override fun areItemsTheSame(
            oldItem: PaidInOutListResp,
            newItem: PaidInOutListResp
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PaidInOutListResp,
            newItem: PaidInOutListResp
        ): Boolean {
            return oldItem == newItem
        }

    }
}