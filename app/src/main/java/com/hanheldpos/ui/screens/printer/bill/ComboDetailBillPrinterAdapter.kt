package com.hanheldpos.ui.screens.printer.bill

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupDetailBillPrinterBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.utils.PriceUtils

class ComboDetailBillPrinterAdapter :
    BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group_detail_bill_printer
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductChosen>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemComboGroupDetailBillPrinterBinding)

        binding.quantity.text = "(${item.Quantity})"
        binding.productName.text = item.Name1
        if(item.ModSubtotal!! > 0) {
            binding.priceTextView.text = "+${PriceUtils.formatStringPrice(item.ModSubtotal.toString())}"
        }

    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductChosen>() {
        override fun areItemsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductChosen, newItem: ProductChosen): Boolean {
            return oldItem == newItem
        }

    }
}