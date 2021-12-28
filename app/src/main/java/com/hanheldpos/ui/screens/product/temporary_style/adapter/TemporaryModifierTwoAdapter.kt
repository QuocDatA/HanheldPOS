package com.hanheldpos.ui.screens.product.temporary_style.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTemporaryCookOptionBinding
import com.hanheldpos.databinding.ItemTemporaryModifierBinding
import com.hanheldpos.databinding.ItemTemporaryVariantBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.screens.cart.adapter.CartDiscountAdapter
import com.hanheldpos.ui.screens.cart.customer.adapter.CustomerAdapter
import com.hanheldpos.ui.screens.combo.adapter.ComboItemPickerAdapter

class TemporaryModifierTwoAdapter : BaseBindingListAdapter<String>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_cook_option;
    }

    val listSelected = mutableListOf<Int>();

    override fun submitList(list: MutableList<String>?) {
        listSelected.clear();
        super.submitList(list)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<String>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemTemporaryCookOptionBinding;
        binding.nameOption.text = item;
        binding.isSelected = listSelected.contains(position)
        binding.root.setOnClickListener {
            if (listSelected.contains(position)) {
                listSelected.remove(position);
            }
            else {
                listSelected.add(position);
            }
            notifyItemChanged(position);
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem;
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem;
        }

    }
}