package com.hanheldpos.ui.screens.discount.discounttype.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDiscountBinding
import com.hanheldpos.databinding.ItemDiscountCompBinding
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class DiscountItemAdapter(private val listener: BaseItemClickListener<BaseProductInCart>) :
    BaseBindingListAdapter<BaseProductInCart>(DiffCallback()) {

    data class SelectedItem(var value: Int = -1)

    private var selectedItem: SelectedItem = SelectedItem(-1)

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_discount;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position);
        val binding = holder.binding as ItemDiscountBinding;
        binding.item = item;
        binding.btnProductItem.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value);
                selectedItem.value = position;
                notifyItemChanged(position);
                listener.onItemClick(position, item);
            }
            isChecked = selectedItem.value == position;
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<BaseProductInCart>() {
        override fun areItemsTheSame(oldItem: BaseProductInCart, newItem: BaseProductInCart): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: BaseProductInCart,
            newItem: BaseProductInCart
        ): Boolean {
            return false
        }

    }
}