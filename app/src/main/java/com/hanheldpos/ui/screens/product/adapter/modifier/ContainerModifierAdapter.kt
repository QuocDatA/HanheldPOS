package com.hanheldpos.ui.screens.product.adapter.modifier

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemContainerModifierBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.adapter.variant.ContainerVariantAdapter

class ContainerModifierAdapter(
    private val listener : BaseItemClickListener<ModifierSelectedItemModel>
    ) : BaseBindingListAdapter<ModifierHeader>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_container_modifier;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ModifierHeader>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemContainerModifierBinding);
        ModifierAdapter(listener = object : BaseItemClickListener<ModifierSelectedItemModel>{
            override fun onItemClick(adapterPosition: Int, item: ModifierSelectedItemModel) {
                listener.onItemClick(adapterPosition,item);
            }

        }).also {
            binding.containerModifierItem.adapter = it;
            binding.containerModifierItem.addItemDecoration(GridSpacingItemDecoration(2, 20, false));
            it.submitList(item.childList);
            it.notifyDataSetChanged()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModifierHeader>() {
        override fun areItemsTheSame(oldItem: ModifierHeader, newItem: ModifierHeader): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: ModifierHeader, newItem: ModifierHeader): Boolean {
            return oldItem == newItem;
        }

    }

}