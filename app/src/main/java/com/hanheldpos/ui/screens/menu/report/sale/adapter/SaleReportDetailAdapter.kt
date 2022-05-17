package com.hanheldpos.ui.screens.menu.report.sale.adapter

import android.graphics.Typeface
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemSaleReportDetailBinding
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class SaleReportDetailAdapter : BaseBindingListAdapter<ReportItemDetail>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_sale_report_detail
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ReportItemDetail>, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ItemSaleReportDetailBinding
        binding.amountLine.text = item.amount
        binding.nameLine.text = item.name
        binding.quantityLine.text = item.qty
        if (item.isBold == true) {
            binding.quantityLine.setTypeface(null,Typeface.BOLD)
            binding.amountLine.setTypeface(null,Typeface.BOLD)
            binding.nameLine.setTypeface(null,Typeface.BOLD)
        }
        if (item.isGray == true){
            binding.quantityLine.setTextColor(binding.root.context.getColor(R.color.color_5))
            binding.amountLine.setTextColor(binding.root.context.getColor(R.color.color_5))
            binding.nameLine.setTextColor(binding.root.context.getColor(R.color.color_5))
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ReportItemDetail>() {
        override fun areItemsTheSame(
            oldItem: ReportItemDetail,
            newItem: ReportItemDetail
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: ReportItemDetail,
            newItem: ReportItemDetail
        ): Boolean {
            return true
        }

    }
}