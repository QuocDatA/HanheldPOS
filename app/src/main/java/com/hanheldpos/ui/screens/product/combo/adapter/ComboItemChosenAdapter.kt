package com.hanheldpos.ui.screens.product.combo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboPickedBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboItemChosenAdapter(
    private val listener: ComboItemChosenListener
) : BaseBindingListAdapter<Regular>(DiffCallback()) {

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
        binding.itemComboModify.setOnClickDebounce {
            listener.onComboItemChoose(action = ItemActionType.Modify, item);
        }
        binding.itemComboRemove.setOnClickDebounce {
            listener.onComboItemChoose(action = ItemActionType.Remove, item);
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