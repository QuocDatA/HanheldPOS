package com.hanheldpos.ui.screens.discount.discount_type.comp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.ItemDiscountCompBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class DiscountReasonAdapter(
    private val comp : Reason?,
    private val listener: BaseItemClickListener<Reason>
) : BaseBindingListAdapter<Reason>(DiffCallback()) {

    data class SelectedItem(var value: Int = -1)

    private var selectedItem: SelectedItem = SelectedItem(-1)

    override fun submitList(list: MutableList<Reason>?) {
        if (comp != null)
        list?.find { it.id == comp.id }.let{
            selectedItem.value =  list?.indexOf(it)?: -1;
        }
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_discount_comp;

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Reason>, position: Int) {
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

    private class DiffCallback : DiffUtil.ItemCallback<Reason>() {
        override fun areItemsTheSame(oldItem: Reason, newItem: Reason): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: Reason,
            newItem: Reason
        ): Boolean {
            return oldItem == newItem;
        }

    }

}