package com.hanheldpos.ui.screens.home.order.combo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboGroupAdapter(

) : BaseBindingListAdapter<ItemComboGroupManager>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group;
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemComboGroupManager>,
        position: Int
    ) {
        val item = getItem(position);
        val binding = holder.binding as ItemComboGroupBinding
        binding.position = (position + 1).toString();
        binding.name = item.getGroupName();
        binding.item = item
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemComboGroupManager>() {
        override fun areItemsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return oldItem.productComboItem?.id == newItem.productComboItem?.id;
        }

        override fun areContentsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return oldItem == newItem;
        }

    }
}