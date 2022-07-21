package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.product.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartProductAdapter(
    private val listener: CartProductListener,
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_product
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemCartProductBinding)
        binding.layoutRoot.setOnClickDebounce { listener.onItemClick(position, item) }
        binding.discountDetail.setClickListener {
            if (!item.discountUsersList.isNullOrEmpty() || !item.discountServersList.isNullOrEmpty())
                listener.onDiscountDelete(
                    position,
                    item
                )
        }
        binding.compDetail.setClickListener {
            if (item.compReason != null)
                listener.onCompDelete(
                    position,
                    item
                )
        }
        if (item is Combo) {
            binding.isShownDetail = item.isShowDetail
            binding.viewDetailBundleTextView.setOnClickDebounce {
                item.isShowDetail = true
                notifyItemChanged(position)
            }
            binding.hideDetailTextView.setOnClickDebounce {
                item.isShowDetail = false
                notifyItemChanged(position)
            }
            val cartComboGroupAdapter = CartComboGroupAdapter(productOrigin = item.proOriginal!!)
            cartComboGroupAdapter.submitList(item.groupList)
            binding.productComboGroupRecyclerView.adapter = cartComboGroupAdapter
        }

        if (item is BuyXGetY) {
            binding.isShownDetail = item.isShowDetail
            binding.viewDetailBuyXGetYTextView.setOnClickDebounce {
                item.isShowDetail = true
                notifyItemChanged(position)
            }
            binding.hideDetailTextView.setOnClickDebounce {
                item.isShowDetail = false
                notifyItemChanged(position)
            }

            val cartBuyXGetYGroupAdapter = CartBuyXGetYGroupAdapter(
                isShowDetail = item.isShowDetail,
                listener = object : CartBuyXGetYGroupAdapter.CartBuyXGetYListener {
                    override fun onItemClick() {
                        listener.onItemClick(holder.bindingAdapterPosition, item)
                    }
                })
            cartBuyXGetYGroupAdapter.submitList(item.groupList)
            binding.productComboGroupRecyclerView.adapter = cartBuyXGetYGroupAdapter
        }

        // disable discount trigger type IN_CART (not showing delete button for this type)
        binding.isDiscountRemovable = false
        if (!item.discountServersList.isNullOrEmpty()) {
            binding.isDiscountRemovable = item.discountServersList?.any { discountResp ->
                (discountResp.isExistsTrigger(DiscountTriggerType.ON_CLICK))
            }
            binding.isDiscountRemovable
        }

        if(!item.discountUsersList.isNullOrEmpty()) {
            binding.isDiscountRemovable = true
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

    interface CartProductListener {
        fun onItemClick(adapterPosition: Int, item: BaseProductInCart)
        fun onDiscountDelete(adapterPosition: Int, item: BaseProductInCart)
        fun onCompDelete(adapterPosition: Int, item: BaseProductInCart)
    }
}