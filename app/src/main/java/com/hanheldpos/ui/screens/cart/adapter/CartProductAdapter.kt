package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartProductAdapter(
    private val listener: CartProductListener,
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_product;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemCartProductBinding);
        binding.layoutTitle.setOnClickListener { listener.onItemClick(position, item); }
        binding.discountDetail.setClickListener {
             if (item.discountUsersList != null)  listener.onDiscountDelete(
                position,
                DiscountCart(disOriginal = item.discountUsersList!!.first(), "", 0.0),
                item
            );
        }
        binding.compDetail.setClickListener {
            if (item.compReason != null) listener.onDiscountDelete(
                position,
                DiscountCart(disOriginal = item.compReason!!, "", 0.0),
                item
            );
        }
        if (item.proOriginal!!.isBundle()) {
            binding.isShownDetail = (item as Combo).isShowDetail;
            binding.viewDetailTextView.setOnClickListener {
                item.isShowDetail = true;
                notifyItemChanged(position);
            }
            binding.hideDetailTextView.setOnClickListener {
                item.isShowDetail = false;
                notifyItemChanged(position);
            }

            val cartComboGroupAdapter = CartComboGroupAdapter();
            cartComboGroupAdapter.submitList(item.groupList)
            binding.productComboGroupRecyclerView.adapter = cartComboGroupAdapter;
        }
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

    interface CartProductListener {
        fun onItemClick(adapterPosition: Int, item: BaseProductInCart);
        fun onDiscountDelete(adapterPosition: Int, discount: DiscountCart, item: BaseProductInCart);
    }
}