package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.CartItemComboGroupBinding
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class CartComboGroupAdapter : BaseBindingListAdapter<GroupBundle>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.cart_item_combo_group;
    }
    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupBundle>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as CartItemComboGroupBinding);
        binding.position=position+1;
        val cartComboGroupDetailAdapter=CartComboGroupDetailAdapter().apply {
            val productList = item.productList.toMutableList()
            submitList(productList as List<BaseProductInCart>);
        }
        binding.cartComboGroupDetailRecyclerView.adapter=cartComboGroupDetailAdapter;
    }

    class DiffCallback : DiffUtil.ItemCallback<GroupBundle>() {
        override fun areItemsTheSame(
            oldItem: GroupBundle,
            newItem: GroupBundle
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: GroupBundle,
            newItem: GroupBundle
        ): Boolean {
            return false
        }

    }
}