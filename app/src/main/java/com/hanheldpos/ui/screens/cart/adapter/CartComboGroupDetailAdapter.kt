package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemCartComboGroupDetailBinding
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartComboGroupDetailAdapter(
    private val productOrigin : Product
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
) {


    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_combo_group_detail;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as ItemCartComboGroupDetailBinding);
        binding.parentItem = productOrigin
    }
    private class DiffCallback : DiffUtil.ItemCallback<BaseProductInCart>() {
        override fun areItemsTheSame(
            oldItem: BaseProductInCart,
            newItem: BaseProductInCart
        ): Boolean {
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