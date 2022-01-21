package com.hanheldpos.ui.screens.menu.option.report.current_drawer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.Report
import com.hanheldpos.databinding.ItemCurrentDrawerInfoBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.utils.PriceHelper

class ReportDrawerInfoAdapter : BaseBindingListAdapter<Report>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_current_drawer_info;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Report>, position: Int) {
        val item = getItem(position);
        val binding = holder.binding as ItemCurrentDrawerInfoBinding

        binding.titleReport.text = item.Title;

        if (item.Value is Double){
            binding.valueReport.text = PriceHelper.formatStringPrice(item.Value.toString());
        }
        else binding.valueReport.text = item.Value.toString();
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem;
        }

    }
}