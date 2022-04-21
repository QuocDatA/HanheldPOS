package com.hanheldpos.ui.screens.printer.bill

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemProductBillPrinterBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.printer.GroupBundlePrinter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ProductBillPrinterAdapter : BaseBindingListAdapter<ProductChosen>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_bill_printer
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductChosen>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemProductBillPrinterBinding)

        val listGroupBundlePrinter: MutableList<GroupBundlePrinter> = mutableListOf()
        val listGroupParent: MutableMap<String?,String?> = mutableMapOf()

        item.ProductChoosedList?.forEach { productChosen ->
            if (!listGroupParent.containsKey(productChosen.Parent_id))
                listGroupParent[productChosen.Parent_id] = productChosen.ParentName
        }

        listGroupParent.forEach { groupParent ->
            item.ProductChoosedList?.filter { product ->
                product.Parent_id?.equals(
                    groupParent.key
                ) == true
            }?.takeIf { it.isNotEmpty() }?.let {
                listGroupBundlePrinter.add(
                    GroupBundlePrinter(
                        groupParent.value,
                        it.toMutableList()
                    )
                )
            }

        }

        if (item.ProductTypeId == ProductType.BUNDLE.value) {
            val comboBillPrinterAdapter = ComboBillPrinterAdapter()
            comboBillPrinterAdapter.submitList(listGroupBundlePrinter)
            binding.listChildProduct.adapter = comboBillPrinterAdapter
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