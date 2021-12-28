package com.hanheldpos.ui.screens.product.temporary_style.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTemporaryCookOptionBinding
import com.hanheldpos.databinding.ItemTemporaryOptionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class TemporaryOptionAdapter : BaseBindingListAdapter<String>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_option;
    }

    var indexSelect : Int = 0;
    var indexDisable : Int = 0;


    override fun submitList(list: MutableList<String>?) {
        indexSelect = 0;
        indexDisable = list?.size?.minus(1) ?: 0;
        super.submitList(list)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<String>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemTemporaryOptionBinding;
        binding.nameOption.text = item;
        binding.isDisable = indexDisable == position;
        binding.isSelected = indexSelect == position;
        binding.root.setOnClickListener {
            if(indexDisable == position) return@setOnClickListener;
            notifyItemChanged(indexSelect);
            indexSelect = position;
            notifyItemChanged(position);
        }
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