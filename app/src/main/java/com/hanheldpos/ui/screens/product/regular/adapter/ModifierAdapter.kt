package com.hanheldpos.ui.screens.product.regular.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemProductModifierBinding
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ModifierAdapter(private val productOrigin : Product, private val listener: BaseItemClickListener<ItemExtra>) :
    BaseBindingListAdapter<ItemExtra>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_modifier
    }


    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemExtra>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemProductModifierBinding)

        binding.parentItem = productOrigin

        binding.btnAddQuantity.setOnClickListener {
            item.addQuantity(1)
            notifyItemChanged(position)
            listener.onItemClick(position, item)

        }
        binding.itemModifierTextBackground.setOnClickListener {
            item.addQuantity(1)
            notifyItemChanged(position)
            listener.onItemClick(position, item)

        }
        binding.btnRemoveQuantity.setOnClickListener {
            item.deleteQuantity(1)
            notifyItemChanged(position)
            listener.onItemClick(position, item)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ItemExtra>() {
        override fun areItemsTheSame(
            oldItem: ItemExtra,
            newItem: ItemExtra
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ItemExtra,
            newItem: ItemExtra
        ): Boolean {
            return oldItem == newItem
        }

    }
}