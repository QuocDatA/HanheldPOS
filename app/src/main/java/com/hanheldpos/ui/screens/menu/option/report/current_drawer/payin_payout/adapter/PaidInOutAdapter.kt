package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.databinding.ItemCashDrawerPaidInOutBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.utils.PriceHelper
import com.hanheldpos.utils.time.DateTimeHelper

class PaidInOutAdapter : BaseBindingListAdapter<PaidInOutListResp>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cash_drawer_paid_in_out;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<PaidInOutListResp>, position: Int) {
        val item = getItem(position);
        val binding = holder.binding as ItemCashDrawerPaidInOutBinding;

        binding.timeApply.text =
            DateTimeHelper.dateToString(
                DateTimeHelper.strToDate(
                    item.CreateDate,
                    DateTimeHelper.Format.FULL_DATE_UTC_NOT_MILI
                ), DateTimeHelper.Format.DD_MM_YYYY_HH_MM
            )
        binding.description.text = item.Description;
        binding.amount.text =
            if (item.Payable ?: 0.0 > 0.0) "-${PriceHelper.formatStringPrice(item.Payable?.toInt().toString())}"
            else
                PriceHelper.formatStringPrice(item.Receivable?.toInt().toString())
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaidInOutListResp>() {
        override fun areItemsTheSame(
            oldItem: PaidInOutListResp,
            newItem: PaidInOutListResp
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: PaidInOutListResp,
            newItem: PaidInOutListResp
        ): Boolean {
            return oldItem == newItem;
        }

    }
}