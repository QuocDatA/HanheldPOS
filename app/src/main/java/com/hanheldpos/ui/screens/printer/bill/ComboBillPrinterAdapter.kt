package com.hanheldpos.ui.screens.printer.bill

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBillPrinterBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.printer.GroupBundlePrinter
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboBillPrinterAdapter : BaseBindingListAdapter<GroupBundlePrinter>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group_bill_printer
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupBundlePrinter>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemComboGroupBillPrinterBinding)
        val pos = position + 1
        binding.positionText.text = pos.toString()
        binding.groupParentName.text = item.productList[0].ParentName

        val comboDetailBillPrinterAdapter = ComboDetailBillPrinterAdapter()
        comboDetailBillPrinterAdapter.submitList(item.productList)

        binding.cartComboGroupDetailRecyclerView.adapter = comboDetailBillPrinterAdapter
    }

    private class DiffCallBack : DiffUtil.ItemCallback<GroupBundlePrinter>() {
        override fun areItemsTheSame(oldItem: GroupBundlePrinter, newItem: GroupBundlePrinter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GroupBundlePrinter, newItem: GroupBundlePrinter): Boolean {
            return oldItem == newItem
        }

    }
}