package com.hanheldpos.ui.screens.home.order.combo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboGroupBinding
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboGroupAdapter(

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
            adapter = item.comboDetailAdapter as ComboItemAdapter;
        }
        binding.itemSelectedAdapter.apply {
            adapter = item.comboItemSelectedAdapter as ComboItemAdapter;
        }

    }

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