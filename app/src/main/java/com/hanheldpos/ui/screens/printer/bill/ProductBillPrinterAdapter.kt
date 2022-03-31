package com.hanheldpos.ui.screens.printer.bill

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemProductBillPrinterBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ProductBillPrinterAdapter : BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_bill_printer
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