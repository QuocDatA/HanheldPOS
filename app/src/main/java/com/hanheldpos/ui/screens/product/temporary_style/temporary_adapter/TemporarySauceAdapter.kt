package com.hanheldpos.ui.screens.product.temporary_style.temporary_adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDiscountBinding
import com.hanheldpos.databinding.ItemTemporaryVariantSauceBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.discount.discounttype.adapter.DiscountItemAdapter
import com.hanheldpos.ui.screens.product.temporary_style.TemporarySauceItem

class TemporarySauceAdapter(private val onTemporarySauceItemClickListener: BaseItemClickListener<TemporarySauceItem>) :
    BaseBindingListAdapter<TemporarySauceItem>(
        DiffCallback(),
    ) {

    data class SelectedItem(var value: Int = -1)

    private var selectedItem: DiscountItemAdapter.SelectedItem =
        DiscountItemAdapter.SelectedItem(-1)

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_temporary_variant_sauce;
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<TemporarySauceItem>,
        position: Int
    ) {
        val item = getItem(position);
        val binding = holder.binding as ItemTemporaryVariantSauceBinding;
        binding.item = item;
        binding.btnProductItem.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value);
                selectedItem.value = position;
                notifyItemChanged(position);
                onTemporarySauceItemClickListener.onItemClick(position, item);
            }
            isChecked = selectedItem.value == position;
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TemporarySauceItem>() {
        override fun areItemsTheSame(
            oldItem: TemporarySauceItem,
            newItem: TemporarySauceItem,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: TemporarySauceItem,
            newItem: TemporarySauceItem
        ): Boolean {
            return false;
        }
    }
}