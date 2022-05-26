package com.hanheldpos.ui.screens.menu.orders.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderOptionFilterBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class FilterOptionAdapter(private val listener : BaseItemClickListener<String>) : BaseBindingListAdapter<String>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_option_filter
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<String>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemOrderOptionFilterBinding
        binding.btnRemove.setOnClickDebounce {
            listener.onItemClick(position,item)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}