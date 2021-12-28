package com.hanheldpos.ui.screens.product.temporary_style.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTemporaryVariantSauceBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener


class TemporarySauceAdapter() :
    BaseBindingListAdapter<String>(
        DiffCallback(),
    ) {

    var indexSelect: Int = 0;

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_variant_sauce;
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<String>,
        position: Int
    ) {
        val item = getItem(position);
        val binding = holder.binding as ItemTemporaryVariantSauceBinding;
        binding.btnProductItem.text = item;
        binding.btnProductItem.apply {
            setOnClickListener {
                notifyItemChanged(indexSelect);
                indexSelect = position;
                notifyItemChanged(position);

            }
            isChecked = indexSelect == position;
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return false;
        }
    }
}