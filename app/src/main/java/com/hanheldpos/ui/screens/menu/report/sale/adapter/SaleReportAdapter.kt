package com.hanheldpos.ui.screens.menu.report.sale.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter

class SaleReportAdapter : BaseBindingListAdapter<ReportItem>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_sale_report
    }



    private class DiffCallBack :  DiffUtil.ItemCallback<ReportItem>() {
        override fun areItemsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean {
            return oldItem == newItem
        }

    }
}