package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener


class CartProductAdapter(
    private val onProductClickListener: BaseItemClickListener<BaseProductInCart>,
) : BaseBindingListAdapter<BaseProductInCart>(
    DiffCallback(),
    itemClickListener = onProductClickListener,
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_product;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as ItemCartProductBinding);

        if(item.proOriginal!!.isBundle())
        {
            binding.isShownDetail=(item as Combo).isShowDetail;
            binding.viewDetailTextView.setOnClickListener {
                item.isShowDetail = true;
                notifyItemChanged(position);
            }
            binding.hideDetailTextView.setOnClickListener {
                item.isShowDetail= false;
                notifyItemChanged(position);
            }

            val cartComboGroupAdapter=CartComboGroupAdapter();
            cartComboGroupAdapter.submitList(item.groupList)
            binding.productComboGroupRecyclerView.adapter=cartComboGroupAdapter;
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<BaseProductInCart>() {
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