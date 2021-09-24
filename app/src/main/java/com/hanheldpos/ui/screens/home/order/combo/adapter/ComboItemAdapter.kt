package com.hanheldpos.ui.screens.home.order.combo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter

class ComboItemAdapter(
    private val listener: ComboItemListener
) : BaseBindingListAdapter<ComboPickedItemViewModel>(DiffCallback()) {

    interface ComboItemListener {
        fun onComboItemChoose(action: ComboItemActionType, item : ComboPickedItemViewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<ComboPickedItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return oldItem.selectedComboItem?.id == newItem.selectedComboItem?.id;
        }

        override fun areContentsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return false
        }

    }
}