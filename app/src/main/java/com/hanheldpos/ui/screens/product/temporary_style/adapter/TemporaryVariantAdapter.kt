package com.hanheldpos.ui.screens.product.temporary_style.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTemporaryVariantBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class TemporaryVariantAdapter : BaseBindingListAdapter<Int>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_variant;
    }

    var indexSelect : Int = 0;
    var indexDisable : Int= 0 ;

    override fun submitList(list: MutableList<Int>?) {
        indexSelect = 0;
        indexDisable = list?.asSequence()?.shuffled()?.find { true }!!;
        super.submitList(list)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Int>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemTemporaryVariantBinding;
        binding.isDisable = indexDisable == position;
        binding.isSelected = indexSelect == position;
        binding.root.setOnClickListener {
            if(indexDisable == position) return@setOnClickListener;
            notifyItemChanged(indexSelect);
            indexSelect = position;
            notifyItemChanged(position);
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem;
        }

    }
}