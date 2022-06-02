package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.databinding.CartItemBuyXGetYGroupBinding
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class CartBuyXGetYGroupAdapter(
    private val isShowDetail: Boolean
) : BaseBindingListAdapter<GroupBuyXGetY>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.cart_item_buy_x_get_y_group
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupBuyXGetY>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)

        val binding = (holder.binding as CartItemBuyXGetYGroupBinding)
        binding.position = position + 1

        val cartBuyXGetYGroupAdapter =
            CartBuyXGetYGroupDetailAdapter(isGroupBuy = item.condition is CustomerBuys)
        cartBuyXGetYGroupAdapter.submitList(item.productList)
        binding.cartComboGroupDetailRecyclerView.adapter = cartBuyXGetYGroupAdapter
        binding.isShowDetail = isShowDetail
    }


    private class DiffCallback : DiffUtil.ItemCallback<GroupBuyXGetY>() {
        override fun areItemsTheSame(
            oldItem: GroupBuyXGetY,
            newItem: GroupBuyXGetY
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GroupBuyXGetY,
            newItem: GroupBuyXGetY
        ): Boolean {
            return false
        }

    }
}