package com.hanheldpos.ui.screens.product.adapter.modifier

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemModifierBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ModifierAdapter(private val listener: BaseItemClickListener<ModifierSelectedItemModel>) :
    BaseBindingListAdapter<ModifierSelectedItemModel>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_modifier;
    }


    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ModifierSelectedItemModel>,
        position: Int
    ) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemModifierBinding);
        binding.btnAddQuantity.setOnClickListener {
            if(item.quantity < item.maxQuantity){
                item.quantity = item.quantity.plus(1);
                notifyItemChanged(position)
                listener.onItemClick(position, item)
            }

        }
        binding.btnRemoveQuantity.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity = item.quantity.minus(1);
                notifyItemChanged(position)
                listener.onItemClick(position, item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModifierSelectedItemModel>() {
        override fun areItemsTheSame(
            oldItem: ModifierSelectedItemModel,
            newItem: ModifierSelectedItemModel
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ModifierSelectedItemModel,
            newItem: ModifierSelectedItemModel
        ): Boolean {
            return oldItem == newItem;
        }

    }
}