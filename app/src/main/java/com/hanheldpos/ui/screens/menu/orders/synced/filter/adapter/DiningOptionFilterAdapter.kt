package com.hanheldpos.ui.screens.menu.orders.synced.filter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.ItemDiningOptionFilterBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class DiningOptionFilterAdapter(private val listener: BaseItemClickListener<DiningOption>) :
    BaseBindingListAdapter<DiningOption>(DiffCallBack()) {

    private var selectedItem: Int = -1

    override fun submitList(list: MutableList<DiningOption>?) {
        selectedItem = 0
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_dining_option_filter
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DiningOption>, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ItemDiningOptionFilterBinding
        binding.item = item
        binding.btn.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem)
                selectedItem = holder.absoluteAdapterPosition
                notifyItemChanged(holder.absoluteAdapterPosition)
                listener.onItemClick(holder.absoluteAdapterPosition, item)
            }
            isChecked = selectedItem == position
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<DiningOption>() {
        override fun areItemsTheSame(oldItem: DiningOption, newItem: DiningOption): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DiningOption, newItem: DiningOption): Boolean {
            return oldItem == newItem
        }

    }
}