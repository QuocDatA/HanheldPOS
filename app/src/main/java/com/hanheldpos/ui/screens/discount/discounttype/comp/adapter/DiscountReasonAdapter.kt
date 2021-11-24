package com.hanheldpos.ui.screens.discount.discounttype.comp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.databinding.ItemDiscountCompBinding
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.discount.discounttype.adapter.DiscountTabAdapter

class DiscountReasonAdapter(
    private val comp : ListReasonsItem?,
    private val listener: BaseItemClickListener<ListReasonsItem>
) : BaseBindingListAdapter<ListReasonsItem>(DiffCallback()) {

    data class SelectedItem(var value: Int = -1)

    private var selectedItem: SelectedItem = SelectedItem(-1)

    override fun submitList(list: MutableList<ListReasonsItem>?) {
        if (comp != null)
        list?.find { it.id == comp.id }.let{
            selectedItem.value =  list?.indexOf(it)?: -1;
        }
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_discount_comp;

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ListReasonsItem>, position: Int) {
        val item = getItem(position);
        val binding = holder.binding as ItemDiscountCompBinding;
        binding.item = item;
        binding.btn.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value);
                selectedItem.value = position;
                notifyItemChanged(position);
                listener.onItemClick(position, item);
            }
            isChecked = selectedItem.value == position;
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ListReasonsItem>() {
        override fun areItemsTheSame(oldItem: ListReasonsItem, newItem: ListReasonsItem): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ListReasonsItem,
            newItem: ListReasonsItem
        ): Boolean {
            return oldItem == newItem;
        }

    }

}