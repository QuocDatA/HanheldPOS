package com.hanheldpos.ui.screens.menu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
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

    private class DiffCallback : DiffUtil.ItemCallback<ItemOptionNav>() {
        override fun areItemsTheSame(
            oldItem: ItemOptionNav,
            newItem: ItemOptionNav,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ItemOptionNav,
            newItem: ItemOptionNav
        ): Boolean {
            return false;
        }
    }
}