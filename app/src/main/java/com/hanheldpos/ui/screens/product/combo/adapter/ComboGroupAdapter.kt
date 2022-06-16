package com.hanheldpos.ui.screens.product.combo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.model.product.combo.ItemComboGroup
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ComboGroupAdapter(
    private val proOriginal: Product,
    private val listener: ItemListener,
) : BaseBindingListAdapter<ItemComboGroup>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_group
    }

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ItemComboGroup>?) {
        selectedItem.value = 0
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemComboGroup>,
        position: Int
    ) {
        // Check your turn
        var positionFocus: Int = -1
        run checkFocus@{
            currentList.forEachIndexed { index, itemComboGroup ->
                if (!itemComboGroup.isMaxItemSelected()) {
                    positionFocus = index
                    return@checkFocus
                }
            }
        }
        val itemComboGroup = getItem(position)
        // Ẩn thông tin combo khi chưa tới lượt
        if (positionFocus == position) {
            selectedItem.value = position
            itemComboGroup.isFocused = true
        } else itemComboGroup.isFocused = false

        val binding = holder.binding as ItemComboGroupBinding
        binding.position = (position + 1).toString()
        binding.name = itemComboGroup.getGroupName()
        binding.item = itemComboGroup

        binding.itemForSelectAdapter.apply {
            adapter = ComboItemPickerAdapter(
                proOriginal = proOriginal,
                groupBundle = itemComboGroup.groupBundle,
                productChosen = itemComboGroup.groupBundle.productList,
                listener = object : BaseItemClickListener<Regular> {
                    override fun onItemClick(adapterPosition: Int, item: Regular) {
                        listener.onProductSelect(
                            itemComboGroup.requireQuantity(),
                            itemComboGroup,
                            item,
                            ItemActionType.Add
                        )
                    }

                }
            ).apply {
                submitList(itemComboGroup.productsForChoose)
            }
        }
        binding.itemSelectedAdapter.apply {
            adapter = ComboItemChosenAdapter(
                listener = object : ComboItemChosenAdapter.ComboItemChosenListener {
                    override fun onComboItemChoose(
                        action: ItemActionType,
                        item: Regular
                    ) {
                        listener.onProductSelect(
                            itemComboGroup.requireQuantity(),
                            itemComboGroup,
                            item,
                            action
                        )
                    }
                }
            ).apply {
                submitList(itemComboGroup.groupBundle.productList)
            }
        }
    }

    interface ItemListener {
        fun onProductSelect(
            maxQuantity: Int,
            group: ItemComboGroup,
            item: Regular,
            actionType: ItemActionType
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<ItemComboGroup>() {
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
            return oldItem.groupBundle == newItem.groupBundle
        }

    }
}