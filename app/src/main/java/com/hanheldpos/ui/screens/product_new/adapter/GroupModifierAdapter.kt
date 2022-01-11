package com.hanheldpos.ui.screens.product_new.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemProductGroupModifierBinding
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierAdapter

class GroupModifierAdapter(
    private val itemSelected: List<ModifierCart>? = null,
    private val listener: BaseItemClickListener<ItemExtra>
) : BaseBindingListAdapter<GroupExtra>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_group_modifier;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupExtra>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemProductGroupModifierBinding);
        ModifierAdapter(listener = object : BaseItemClickListener<ItemExtra> {
            override fun onItemClick(adapterPosition: Int, item: ItemExtra) {
                listener.onItemClick(adapterPosition, item);
            }
        }).also { modifierAdapter ->
            binding.containerModifierItem.adapter = modifierAdapter;
            binding.containerModifierItem.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    20,
                    false
                )
            );
            /**
             * fix update quantity scroll to top
             */
            binding.containerModifierItem.setHasFixedSize(true);

            /**
             * Restore option choose
             * */
            val list = item.modifierList;
            itemSelected?.forEach { it1 ->
                run lit@{
                    list.forEach { it2 ->
                        if (it1.modifierId == it2.modifier.id) {
                            it2.extraQuantity = it1.quantity;
                            return@lit
                        }
                    }
                }
            }
            modifierAdapter.submitList(item.modifierList);
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<GroupExtra>() {
        override fun areItemsTheSame(oldItem: GroupExtra, newItem: GroupExtra): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: GroupExtra, newItem: GroupExtra): Boolean {
            return oldItem == newItem;
        }

    }
}