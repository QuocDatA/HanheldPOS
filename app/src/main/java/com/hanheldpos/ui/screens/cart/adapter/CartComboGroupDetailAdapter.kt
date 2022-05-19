package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemBuyXGetYComboGroupDetailBinding
import com.hanheldpos.databinding.ItemCartComboGroupDetailBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartComboGroupDetailAdapter(
    private val groupBundle: GroupBundle,
    private val productBundle: Product,
    private val isBuyXGetY: Boolean? = false
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
) {


    override fun getItemViewType(position: Int): Int {
        return if (isBuyXGetY == true) R.layout.item_buy_x_get_y_combo_group_detail else R.layout.item_cart_combo_group_detail
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        if (isBuyXGetY == true) {
            val binding =
                (holder.binding as ItemBuyXGetYComboGroupDetailBinding)
            binding.productBundle = productBundle
            binding.groupBundle = groupBundle
        } else {
            val binding = (holder.binding as ItemCartComboGroupDetailBinding)
            binding.productBundle = productBundle
            binding.groupBundle = groupBundle
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