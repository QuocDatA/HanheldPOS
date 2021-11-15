package com.hanheldpos.ui.screens.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener


class CartProductAdapter(
    private val onProductClickListener: BaseItemClickListener<OrderItemModel>,
) : BaseBindingListAdapter<OrderItemModel>(
    DiffCallback(),
    itemClickListener = onProductClickListener,
) {
    override fun submitList(
        products: MutableList<OrderItemModel>?,
    ) {
        super.submitList(products)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_product;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<OrderItemModel>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as ItemCartProductBinding);
        binding.isShownDetail=item.isShownDetail;
        if(item.isCombo())
        {
//            binding.viewDetailTextView.setOnClickListener {
//               item.isShownDetail= true;
//                notifyItemChanged(position);
//            }
//            binding.hideDetailTextView.setOnClickListener {
//                item.isShownDetail= false;
//                notifyItemChanged(position);
//            }
//
//            val cartComboGroupAdapter=CartComboGroupAdapter();
//            cartComboGroupAdapter.submitList(item.menuComboItem!!.listItemsByGroup!!.toMutableList())
//            binding.productComboGroupRecyclerView.adapter=cartComboGroupAdapter;
        }
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