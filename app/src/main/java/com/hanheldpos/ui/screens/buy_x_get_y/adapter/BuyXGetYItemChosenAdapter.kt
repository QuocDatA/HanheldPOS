package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.databinding.ItemComboPickedBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class BuyXGetYItemChosenAdapter() : BaseBindingListAdapter<Regular>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_picked
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<Regular>,
        position: Int
    ) {
        val item = getItem(position);
        val binding = holder.binding as ItemComboPickedBinding;
        binding.item = item;
        binding.itemComboModify.setOnClickListener {
            //listener.onComboItemChoose(action = ItemActionType.Modify, item);
        }
        binding.itemComboRemove.setOnClickListener {
            //listener.onComboItemChoose(action = ItemActionType.Remove, item);
        }
    }

    interface ComboItemChosenListener {
        fun onComboItemChoose(action: ItemActionType, item: Regular)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Regular>() {
        override fun areItemsTheSame(
            oldItem: Regular,
            newItem: Regular
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: Regular,
            newItem: Regular
        ): Boolean {
            return false
        }

    }
}