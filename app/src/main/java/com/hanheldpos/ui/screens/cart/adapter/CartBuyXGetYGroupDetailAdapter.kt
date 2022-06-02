package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.CartItemBuyXGetYComboGroupBinding
import com.hanheldpos.databinding.ItemCartBuyXGetYGroupDetailBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartBuyXGetYGroupDetailAdapter(
    private val isGroupBuy: Boolean ? = false,
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.proOriginal?.isBundle() != true) R.layout.item_cart_buy_x_get_y_group_detail
        else R.layout.cart_item_buy_x_get_y_combo_group
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        if (!item.proOriginal?.isBundle()!!) {
            val binding =
                (holder.binding as ItemCartBuyXGetYGroupDetailBinding)
            binding.item = item
            binding.productBundle = item.proOriginal
            binding.isGroupBuy = isGroupBuy
        } else {
            val binding = (holder.binding as CartItemBuyXGetYComboGroupBinding)
            binding.item = item as Combo

            val cartComboGroupAdapter =
                CartComboGroupAdapter(productOrigin = item.proOriginal!!, isBuyXGetY = true, isInCart = true)
            cartComboGroupAdapter.submitList(item.groupList)
            binding.cartComboGroupDetailRecyclerView.adapter = cartComboGroupAdapter
        }
    }
    private class DiffCallback : DiffUtil.ItemCallback<BaseProductInCart>() {
        override fun areItemsTheSame(
            oldItem: BaseProductInCart,
            newItem: BaseProductInCart
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BaseProductInCart,
            newItem: BaseProductInCart
        ): Boolean {
            return false
        }

    }
}