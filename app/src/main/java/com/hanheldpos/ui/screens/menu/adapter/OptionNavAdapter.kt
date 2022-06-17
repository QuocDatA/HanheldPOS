package com.hanheldpos.ui.screens.menu.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemMenuBinding
import com.hanheldpos.model.menu.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OptionNavAdapter(
    private val onMenuItemClickListener: BaseItemClickListener<ItemOptionNav>,
) : BaseBindingListAdapter<ItemOptionNav>(
    DiffCallback(),
    itemClickListener = onMenuItemClickListener
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_menu
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ItemOptionNav>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemMenuBinding
        if (item.type == NavBarOptionType.UPDATE_DATA && item.value != null && item.value is Boolean && item.value == true){
            binding.tagNew.visibility = View.VISIBLE
        }else {
            binding.tagNew.visibility = View.GONE
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ItemOptionNav>() {
        override fun areItemsTheSame(
            oldItem: ItemOptionNav,
            newItem: ItemOptionNav,
        ): Boolean {
            return oldItem.type == newItem.type;
        }

        override fun areContentsTheSame(
            oldItem: ItemOptionNav,
            newItem: ItemOptionNav
        ): Boolean {
            return oldItem == newItem;
        }
    }
}