package com.hanheldpos.ui.screens.menu.report.sale.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemSalesReportDayNumberBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class NumberDayReportAdapter(private val listener : BaseItemClickListener<NumberDayReportItem>) : BaseBindingListAdapter<NumberDayReportItem>(
    DiffCallback()
) {

    private var selectedItem : Int = -1;

    override fun submitList(list: MutableList<NumberDayReportItem>?) {
        this.selectedItem = -1;
        super.submitList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun clearSelected() {
        this.selectedItem = -1;
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_sales_report_day_number;
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<NumberDayReportItem>,
        position: Int
    ) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemSalesReportDayNumberBinding;
        binding.root.apply {

            setOnClickListener {
                notifyItemChanged(selectedItem);
                selectedItem = holder.bindingAdapterPosition;
                notifyItemChanged(position);
            }

            if(selectedItem == position){
                binding.title.setTextColor(binding.root.context.getColor(R.color.color_0))
                listener.onItemClick(position,item);
            }
            else {
                binding.title.setTextColor(binding.root.context.getColor(R.color.color_4));
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<NumberDayReportItem>() {
        override fun areItemsTheSame(
            oldItem: NumberDayReportItem,
            newItem: NumberDayReportItem
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: NumberDayReportItem,
            newItem: NumberDayReportItem
        ): Boolean {
            return oldItem == newItem;
        }

    }
}