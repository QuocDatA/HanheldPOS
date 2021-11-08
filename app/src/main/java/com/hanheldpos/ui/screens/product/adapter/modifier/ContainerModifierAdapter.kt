package com.hanheldpos.ui.screens.product.adapter.modifier

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemContainerModifierBinding
import com.hanheldpos.extension.mergeList
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.adapter.variant.ContainerVariantAdapter
import kotlinx.coroutines.flow.merge

class ContainerModifierAdapter(
    private val itemSeleted: List<ModifierSelectedItemModel>? = null,
    private val listener: BaseItemClickListener<ModifierSelectedItemModel>
) : BaseBindingListAdapter<ModifierHeader>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_container_modifier;
    }


    override fun onBindViewHolder(holder: BaseBindingViewHolder<ModifierHeader>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemContainerModifierBinding);
        ModifierAdapter(listener = object : BaseItemClickListener<ModifierSelectedItemModel> {
            override fun onItemClick(adapterPosition: Int, item: ModifierSelectedItemModel) {
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
            var list = item.childList;
            itemSeleted?.forEach { it1 ->
                run lit@{
                    list?.forEach { it2 ->
                        if (it1.realItem?.id == it2.realItem?.id) {
                            it2.quantity = it1.quantity;
                            return@lit
                        }
                    }
                }
            }
            modifierAdapter.submitList(item.childList);
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