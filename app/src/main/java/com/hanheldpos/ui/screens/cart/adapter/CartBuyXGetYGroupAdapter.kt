package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.databinding.CartItemBuyXGetYGroupBinding
import com.hanheldpos.model.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.buy_x_get_y.CustomerDiscApplyTo
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.screens.cart.CurCartData

class CartBuyXGetYGroupAdapter(
    private val isShowDetail: Boolean, private val listener : CartBuyXGetYListener,
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
            CartBuyXGetYGroupDetailAdapter(
                isGroupBuy = item.condition is CustomerBuys,
                isShowDetail
            )
        cartBuyXGetYGroupAdapter.submitList(item.productList)
        binding.cartComboGroupDetailRecyclerView.adapter = cartBuyXGetYGroupAdapter

        binding.btnContinueBuyXGetY.setOnClickListener {
            listener.onItemClick()
        }

        // variable to check complete status for entire order
        val totalOrder = CurCartData.cartModel!!.total()
        val totalQuantityOrder = CurCartData.cartModel!!.getBuyXGetYQuantity(item.parentDisc_Id)

        binding.isComplete = if (item.condition is CustomerBuys) {
            if (CustomerDiscApplyTo.fromInt((item.condition as CustomerBuys).ApplyTo) != CustomerDiscApplyTo.ENTIRE_ORDER)
                (item.condition as CustomerBuys).isBuyCompleted(item.totalPrice, item.totalQuantity)
            else (item.condition as CustomerBuys).isBuyCompleted(
                totalOrder,
                totalQuantityOrder
            )
        } else {
            if (CustomerDiscApplyTo.fromInt((item.condition as CustomerGets).ApplyTo) != CustomerDiscApplyTo.ENTIRE_ORDER)
                item.isCompleted
            else
                true
        }
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

    interface CartBuyXGetYListener {
        fun onItemClick()
    }
}