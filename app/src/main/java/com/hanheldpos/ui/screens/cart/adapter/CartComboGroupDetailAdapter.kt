package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartComboGroupDetailBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CartComboGroupDetailAdapter(
) : BaseBindingListAdapter<OrderItemModel>(
    DiffCallback(),
) {
    override fun submitList(
        products: MutableList<OrderItemModel>?,
    ) {
        super.submitList(products)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_combo_group_detail;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<OrderItemModel>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as ItemCartComboGroupDetailBinding);



    }
    class DiffCallback : DiffUtil.ItemCallback<OrderItemModel>() {
        override fun areItemsTheSame(
            oldItem: OrderItemModel,
            newItem: OrderItemModel
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: OrderItemModel,
            newItem: OrderItemModel
        ): Boolean {
            return false
        }

    }
}