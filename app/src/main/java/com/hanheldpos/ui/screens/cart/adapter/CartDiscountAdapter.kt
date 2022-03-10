package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartDiscountBinding
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CartDiscountAdapter(private val listener : BaseItemClickListener<DiscountCart>) : BaseBindingListAdapter<DiscountCart>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_discount
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DiscountCart>, position: Int) {
        val discount = getItem(position)
        holder.bindItem(discount)

        (holder.binding as ItemCartDiscountBinding).btnRemove.setOnClickListener {
            listener.onItemClick(position,discount)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<DiscountCart>() {
        override fun areItemsTheSame(oldItem: DiscountCart, newItem: DiscountCart): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DiscountCart, newItem: DiscountCart): Boolean {
            return oldItem == newItem
        }

    }
}