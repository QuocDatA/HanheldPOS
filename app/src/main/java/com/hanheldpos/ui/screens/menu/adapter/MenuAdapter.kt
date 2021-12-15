package com.hanheldpos.ui.screens.menu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.menu.MenuFragment.FakeMenuItem

class MenuAdapter(
    private val onMenuItemClickListener: BaseItemClickListener<FakeMenuItem>,
) : BaseBindingListAdapter<FakeMenuItem>(
    DiffCallback(),
    itemClickListener = onMenuItemClickListener
) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_menu
    }

    class DiffCallback : DiffUtil.ItemCallback<FakeMenuItem>() {
        override fun areItemsTheSame(
            oldItem: FakeMenuItem,
            newItem: FakeMenuItem,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: FakeMenuItem,
            newItem: FakeMenuItem
        ): Boolean {
            return false;
        }
    }
}