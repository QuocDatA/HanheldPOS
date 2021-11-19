package com.hanheldpos.ui.screens.combo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ComboGroupAdapter(
    private val listener: ItemListener,
) : BaseBindingListAdapter<ItemComboGroup>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group;
    }

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ItemComboGroup>?) {
        selectedItem.value = 0;
        super.submitList(list);
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemComboGroup>,
        position: Int
    ) {
        /**
         * Check tới lượt chọn
         */
        var positionFocus: Int = -1;
        run checkFocus@{
            currentList.forEachIndexed { index, itemComboGroup->
                if (!itemComboGroup.isMaxItemSelected()) {
                    positionFocus = index;
                    return@checkFocus;
                }
            }
        }
        val itemComboGroup = getItem(position);
        // Ẩn thông tin combo khi chưa tới lượt
        if (positionFocus == position) {
            selectedItem.value = position;
            itemComboGroup.isFocused = true;
        } else itemComboGroup.isFocused = false;

        val binding = holder.binding as ItemComboGroupBinding
        binding.position = (position + 1).toString();
        binding.name = itemComboGroup.getGroupName();
        binding.item = itemComboGroup;

        /*
        * Green tick item has chosen in list
        * */
//        item.productsForChoose.forEach { it.isChosen = false }
        itemComboGroup.groupBundle.productList.forEach { regular ->
            itemComboGroup.productsForChoose.find { pro-> regular.proOriginal?.id == pro.proOriginal?.id }.let {
//                it?.isChosen = true;
            }
        }
        binding.itemForSelectAdapter.apply {
            adapter = ComboItemPickerAdapter(
                listener = object : BaseItemClickListener<Regular> {
                    override fun onItemClick(adapterPosition: Int, item: Regular) {
                        listener.onProductSelect(itemComboGroup.requireQuantity(),itemComboGroup,item,ItemActionType.Add);
                    }

                }
            ).apply {
                submitList(itemComboGroup.productsForChoose);
            };
        }
        binding.itemSelectedAdapter.apply {
            adapter = ComboItemChosenAdapter(
                listener = object : ComboItemChosenAdapter.ComboItemChosenListener {
                    override fun onComboItemChoose(
                        action: ItemActionType,
                        itemClick: Regular
                    ) {
                        listener.onProductSelect(itemComboGroup.requireQuantity(),itemComboGroup,itemClick,action);
                    }
                }
            ).apply {
                submitList(itemComboGroup.groupBundle.productList);
            };
        }
    }

    interface ItemListener {
        fun onProductSelect(
            maxQuantity: Int,
            group: ItemComboGroup,
            item: Regular,
            actionType: ItemActionType
        );

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

    class DiffCallback : DiffUtil.ItemCallback<ItemComboGroup>() {
        override fun areItemsTheSame(
            oldItem: ItemComboGroup,
            newItem: ItemComboGroup
        ): Boolean {
            return oldItem.groupBundle == newItem.groupBundle
        }

        override fun areContentsTheSame(
            oldItem: ItemComboGroup,
            newItem: ItemComboGroup
        ): Boolean {
            return oldItem.groupBundle == newItem.groupBundle;
        }

    }
}