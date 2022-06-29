package com.hanheldpos.printer.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemPrinterChooseBinding
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

data class PrinterChooseModel(
    val printerId: String?,
    val printerName: String?,
    val isChecked: Boolean = false
)

class PrinterChooseAdapter(private val onPrinterChecked: (position: Int, checked: Boolean) -> Unit) :
    BaseBindingListAdapter<PrinterChooseModel>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_printer_choose
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<PrinterChooseModel>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemPrinterChooseBinding
        binding.checkbox.isChecked = item.isChecked
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onPrinterChecked.invoke(position, isChecked)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<PrinterChooseModel>() {
        override fun areItemsTheSame(
            oldItem: PrinterChooseModel,
            newItem: PrinterChooseModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PrinterChooseModel,
            newItem: PrinterChooseModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}