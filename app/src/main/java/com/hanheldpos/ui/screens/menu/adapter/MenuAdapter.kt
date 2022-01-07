package com.hanheldpos.ui.screens.menu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.menu_nav_opt.ItemMenuOptionNav
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class MenuAdapter(
    private val onMenuItemClickListener: BaseItemClickListener<ItemMenuOptionNav>,
) : BaseBindingListAdapter<ItemMenuOptionNav>(
    DiffCallback(),
    itemClickListener = onMenuItemClickListener
) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_menu
    }


    private class DiffCallback : DiffUtil.ItemCallback<ItemMenuOptionNav>() {
        override fun areItemsTheSame(
            oldItem: ItemMenuOptionNav,
            newItem: ItemMenuOptionNav,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ItemMenuOptionNav,
            newItem: ItemMenuOptionNav
        ): Boolean {
            return false;
        }
    }
}