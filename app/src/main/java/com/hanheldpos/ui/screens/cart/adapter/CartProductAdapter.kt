package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.BaseProductInCart
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
            binding.viewDetailTextView.setOnClickDebounce {
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