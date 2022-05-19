package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.BuyXGetYItemComboGroupBinding
import com.hanheldpos.databinding.CartItemComboGroupBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class CartComboGroupAdapter(
    private val productOrigin: Product,
    private val isBuyXGetY: Boolean? = false
) : BaseBindingListAdapter<GroupBundle>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (isBuyXGetY == false) R.layout.cart_item_combo_group else R.layout.buy_x_get_y_item_combo_group
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupBundle>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)

        if (isBuyXGetY == false) {
            val binding = (holder.binding as CartItemComboGroupBinding)
            binding.position = position + 1
            val cartComboGroupDetailAdapter = CartComboGroupDetailAdapter(
                groupBundle = item,
                productBundle = productOrigin
            ).apply {
                val productList = item.productList.toMutableList()
                submitList(productList as List<BaseProductInCart>)
            }
            binding.cartComboGroupDetailRecyclerView.adapter = cartComboGroupDetailAdapter
        } else {
            val binding = (holder.binding as BuyXGetYItemComboGroupBinding)
            binding.position = position + 1
            val cartComboGroupDetailAdapter = CartComboGroupDetailAdapter(
                groupBundle = item,
                productBundle = productOrigin,
                isBuyXGetY = true
            ).apply {
                val productList = item.productList.toMutableList()
                submitList(productList as List<BaseProductInCart>)
            }
            binding.cartComboGroupDetailRecyclerView.adapter = cartComboGroupDetailAdapter
        }

    }


    private class DiffCallback : DiffUtil.ItemCallback<GroupBundle>() {
        override fun areItemsTheSame(
            oldItem: GroupBundle,
            newItem: GroupBundle
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GroupBundle,
            newItem: GroupBundle
        ): Boolean {
            return false
        }

    }
}