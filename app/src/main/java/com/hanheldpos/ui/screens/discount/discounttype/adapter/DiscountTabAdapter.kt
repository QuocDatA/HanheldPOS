package com.hanheldpos.ui.screens.discount.discounttype.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemTabDiscountTypeBinding
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.variant.VariantAdapter

class DiscountTabAdapter(
    private val listener: BaseItemClickListener<DiscountTypeTab>
) : BaseBindingListAdapter<DiscountTypeTab>(DiffCallback(), listener) {
    data class SelectedItem(var value: Int = 0)
    private var selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<DiscountTypeTab>?) {
        selectedItem.value = 0;
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_tab_discount_type;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DiscountTypeTab>, position: Int) {
        val item = getItem(position)

        holder.bindItem(item);
        val binding = holder.binding as ItemTabDiscountTypeBinding
        binding.root.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value);
                selectedItem.value = position;
                notifyItemChanged(position);
                listener.onItemClick(position, item);
            }

        }
        binding.isSelected = position == selectedItem.value;

    }

    private class DiffCallback : DiffUtil.ItemCallback<DiscountTypeTab>() {

        override fun areItemsTheSame(oldItem: DiscountTypeTab, newItem: DiscountTypeTab): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: DiscountTypeTab,
            newItem: DiscountTypeTab
        ): Boolean {
            return oldItem == newItem
        }

    }
}