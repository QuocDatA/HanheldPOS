package com.hanheldpos.ui.screens.product.adapter.variant

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemContainerVarientBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration

class ContainerVariantAdapter(
    private val listener: BaseItemClickListener<VariantLayoutItem>
) : BaseBindingListAdapter<VariantHeader>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_container_varient;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VariantHeader>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemContainerVarientBinding);
        VariantAdapter(listener = object : BaseItemClickListener<VariantLayoutItem> {
            override fun onItemClick(adapterPosition: Int, item: VariantLayoutItem) {
                listener.onItemClick(adapterPosition, item);
            }
        }).also {
            binding.containerVariantItem.adapter = it;
            binding.containerVariantItem.addItemDecoration(GridSpacingItemDecoration(2, 20, false))
            it.submitList(item.childList?.toMutableList());
            it.notifyDataSetChanged();
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VariantHeader>() {

        override fun areItemsTheSame(oldItem: VariantHeader, newItem: VariantHeader): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: VariantHeader, newItem: VariantHeader): Boolean {
            return oldItem == newItem;
        }

    }
}