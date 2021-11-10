package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.CartItemComboGroupBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class CartComboGroupAdapter :BaseBindingListAdapter<ItemComboGroupManager>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.cart_item_combo_group;
    }
    override fun onBindViewHolder(holder: BaseBindingViewHolder<ItemComboGroupManager>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as CartItemComboGroupBinding);
        binding.position=position+1;
        val cartComboGroupDetailAdapter=CartComboGroupDetailAdapter().apply {
            val selectedOrderMenuItems=item.listSelectedComboItems.map {
                it!!.selectedComboItem as OrderItemModel
            }.toMutableList();
            submitList(selectedOrderMenuItems);
        }
        binding.cartComboGroupDetailRecyclerView.adapter=cartComboGroupDetailAdapter;
    }
    override fun submitList(
        groups: MutableList<ItemComboGroupManager?>?,
    ) {
        super.submitList(groups)
    }
    class DiffCallback : DiffUtil.ItemCallback<ItemComboGroupManager>() {
        override fun areItemsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return false
        }

    }
}