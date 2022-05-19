package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemBuyXGetYComboPickedBinding
import com.hanheldpos.databinding.ItemComboPickedBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.screens.cart.adapter.CartComboGroupAdapter

class BuyXGetYItemChosenAdapter(
    private val listener: ComboItemChosenListener,
) : BaseBindingListAdapter<BaseProductInCart>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (!item.proOriginal?.isBundle()!!) R.layout.item_combo_picked else R.layout.item_buy_x_get_y_combo_picked
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<BaseProductInCart>,
        position: Int
    ) {
        val item = getItem(position);
        if (!item.proOriginal?.isBundle()!!) {
            val binding = holder.binding as ItemComboPickedBinding;
            binding.item = item as Regular;
            binding.itemComboModify.setOnClickListener {
                listener.onComboItemChoose(action = ItemActionType.Modify, item);
            }
            binding.itemComboRemove.setOnClickListener {
                listener.onComboItemChoose(action = ItemActionType.Remove, item);
            }
        } else {
            val binding = holder.binding as ItemBuyXGetYComboPickedBinding
            binding.item = item as Combo;
            val cartComboGroupAdapter =
                CartComboGroupAdapter(productOrigin = item.proOriginal!!, isBuyXGetY = true)
            cartComboGroupAdapter.submitList(item.groupList)
            binding.productComboGroupRecyclerView.adapter = cartComboGroupAdapter

            binding.itemComboModify.setOnClickListener {
                listener.onComboItemChoose(action = ItemActionType.Modify, item);
            }
            binding.itemComboRemove.setOnClickListener {
                listener.onComboItemChoose(action = ItemActionType.Remove, item);
            }
        }

    }

    interface ComboItemChosenListener {
        fun onComboItemChoose(action: ItemActionType, item: BaseProductInCart)
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
}