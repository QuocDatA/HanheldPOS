package com.hanheldpos.ui.screens.combo.adapter

import androidx.core.view.size
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import kotlin.math.abs

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
        val item = getItem(position);
        // Ẩn thông tin combo khi chưa tới lượt
        if (positionFocus == position) {
            selectedItem.value = position;
            item.isFocused = true;
        } else item.isFocused = false;

        val binding = holder.binding as ItemComboGroupBinding
        binding.position = (position + 1).toString();
        binding.name = item.getGroupName();
        binding.item = item;

        /*
        * Green tick item has chosen in list
        * */
        item.productsForChoose.forEach { it.isChosen = false }
        item.groupBundle.productList.forEach { regular ->
            item.productsForChoose.find { pro-> regular.proOriginal?.id == pro.proOriginal?.id }.let {
                it?.isChosen = true;
            }
        }
        binding.itemForSelectAdapter.apply {
            adapter = ComboItemPickerAdapter(
                listener = object : BaseItemClickListener<ProductMenuItem> {
                    override fun onItemClick(adapterPosition: Int, itemClick: ProductMenuItem) {
                        listener.onProductSelect(item.requireQuantity(),item,itemClick);
                    }

                }
            ).apply {
                submitList(item.productsForChoose);
            };
        }
        binding.itemSelectedAdapter.apply {
            adapter = ComboItemChosenAdapter(
                listener = object : ComboItemChosenAdapter.ComboItemChosenListener {
                    override fun onComboItemChoose(
                        action: ItemActionType,
                        itemClick: Regular
                    ) {
                        comboItemAction(item, action, itemClick);
                    }
                }
            ).apply {
                submitList(item.groupBundle.productList);
            };
        }
    }

    interface ItemListener {
        fun onProductSelect(
            maxQuantity: Int,
            group: ItemComboGroup,
            item: ProductMenuItem,
        );
    }

    fun comboItemAction(
        comboManager: ItemComboGroup,
        action: ItemActionType,
        item: Regular
    ) {
//        when (action) {
//            ItemActionType.Add -> {
//                listener.onProductSelect(
//                    comboManager.requireQuantity(),
//                    comboManager,
//                    item.clone(),
//                    action
//                );
//            }
//            ItemActionType.Modify -> {
//                listener.onProductSelect(
//                    comboManager.requireQuantity() + (item.selectedComboItem?.quantity
//                        ?: 0),
//                    comboManager,
//                    item,
//                    action
//                );
//            }
//            ItemActionType.Remove -> {
//                listener.onProductSelect(
//                    comboManager.requireQuantity() + (item.selectedComboItem?.quantity
//                        ?: 0),
//                    comboManager,
//                    item,
//                    action
//                );
//            }
//        }
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