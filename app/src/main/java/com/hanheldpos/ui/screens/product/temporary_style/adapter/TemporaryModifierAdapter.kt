package com.hanheldpos.ui.screens.product.temporary_style.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTemporaryCookOptionBinding
import com.hanheldpos.databinding.ItemTemporaryModifierBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class TemporaryModifierAdapter : BaseBindingListAdapter<String>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_modifier;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<String>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemTemporaryModifierBinding;
        binding.nameModifier.text = item;
        binding.isImage = mutableListOf<Boolean>(true,true,false,false).asSequence().shuffled().find { true };
    }

    private class DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem;
        }

    }
}