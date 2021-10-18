package com.hanheldpos.ui.screens.home.order.combo.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import kotlinx.parcelize.RawValue

class ComboGroupAdapter(
    private val listener : ItemListener,
) : BaseBindingListAdapter<ItemComboGroupManager>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group;
    }

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ItemComboGroupManager>?) {
        selectedItem.value = 0;
        super.submitList(list);
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemComboGroupManager>,
        position: Int
    ) {
        /**
         * Check tới lượt chọn
        */
        var groupTurn : String? = null
        currentList.forEach {
            if (!it.isMaxItemSelected() && groupTurn == null)
            {
                groupTurn = it.productComboItem?.comboGuid;
            }
        }

        val item = getItem(position);
        // Ẩn thông tin combo khi chưa tới lượt
        item.isFocused = groupTurn == item.productComboItem?.comboGuid
        val binding = holder.binding as ItemComboGroupBinding
        binding.position = (position + 1).toString();
        binding.name = item.getGroupName();
        binding.item = item;

        binding.itemForSelectAdapter.apply {
            adapter = ComboItemAdapter(
                modeViewType = ComboItemAdapter.ComboItemViewType.ForChoose,
                listener = object : ComboItemAdapter.ComboItemListener {
                    override fun onComboItemChoose(
                        action: ItemActionType,
                        itemClick: ComboPickedItemViewModel
                    ) {
                        comboItemAction(item, action, itemClick);
                        notifyItemChanged(holder.bindingAdapterPosition);
                    }
                }
            ).apply {
                submitList(item.productsForChoose);
            };
        }
        binding.itemSelectedAdapter.apply {
            adapter =  ComboItemAdapter(
                modeViewType = ComboItemAdapter.ComboItemViewType.Chosen,
                listener = object : ComboItemAdapter.ComboItemListener {
                    override fun onComboItemChoose(
                        action: ItemActionType,
                        itemClick: ComboPickedItemViewModel
                    ) {
                        comboItemAction(item, action, itemClick);
                        notifyDataSetChanged();
                    }
                }
            ).apply {
                submitList(item.listSelectedComboItems);
            };
        }
    }

    interface ItemListener {
        fun onProductSelect(maxQuantity: Int, comboManager: ItemComboGroupManager , item: ComboPickedItemViewModel, action: ItemActionType);
    }

    fun comboItemAction(
        comboManager: ItemComboGroupManager,
        action: ItemActionType,
        item: ComboPickedItemViewModel
    ) {
        when (action) {
            ItemActionType.Add -> {
                listener.onProductSelect(
                    comboManager.requireQuantity(),
                    comboManager,
                    item.clone(),
                    action
                );
            }
            ItemActionType.Modify -> {
                listener.onProductSelect(
                    comboManager.requireQuantity() + (item.selectedComboItem?.quantity
                        ?: 0),
                    comboManager,
                    item,
                    action
                );
            }
            ItemActionType.Remove -> {
                /**
                 * Delete green tick when remove item
                 */
                item.isChosen = false;
                comboManager.listSelectedComboItems.remove(item)
                return;
            }
        }
    }


    /*private fun toggleItemSelected(
        comboGroup: Any?,
        currentItem: ComboPickedItemViewModel,
        value: Boolean
    ) {
        (comboGroup as ComboItemAdapter).let {
            val currentItem =
                it.currentList.find { temp -> temp.selectedComboItem?.productOrderItem?.id == currentItem.selectedComboItem?.productOrderItem?.id };
            val currentItemIndex = it.currentList.indexOf(currentItem);
            currentItem?.isChosen = value;
            it.notifyItemChanged(currentItemIndex);
        };
    }*/

    class DiffCallback : DiffUtil.ItemCallback<ItemComboGroupManager>() {
        override fun areItemsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return oldItem.productComboItem?.id == newItem.productComboItem?.id;
        }

        override fun areContentsTheSame(
            oldItem: ItemComboGroupManager,
            newItem: ItemComboGroupManager
        ): Boolean {
            return false;
        }

    }
}