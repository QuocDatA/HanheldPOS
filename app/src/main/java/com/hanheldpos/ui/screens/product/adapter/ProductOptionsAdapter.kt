package com.hanheldpos.ui.screens.product.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductOption
import com.hanheldpos.databinding.ItemProductOptionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ProductOptionsAdapter(
    private val listener: BaseItemClickListener<ProductOption>
) : BaseBindingListAdapter<ProductOption>(DiffCallBack(), listener) {

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ProductOption>?) {
        selectedItem.value = 0
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_option
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductOption>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val bindingHolder = (holder.binding as ItemProductOptionBinding)
        bindingHolder.radioBtnSelect.apply {
            setOnClickListener {
                this@ProductOptionsAdapter.notifyItemChanged(selectedItem.value)
                selectedItem.value = position
                this@ProductOptionsAdapter.notifyItemChanged(position)
                listener.onItemClick(position, item)
            }
            if (selectedItem.value == position) {
                isChecked = true
                bindingHolder.radioBtnSelect.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primary
                    )
                )
            } else {
                isChecked = false
                bindingHolder.radioBtnSelect.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.nearlyBlack
                    )
                )
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductOption>() {
        override fun areItemsTheSame(oldItem: ProductOption, newItem: ProductOption): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductOption, newItem: ProductOption): Boolean {
            return oldItem == newItem
        }

    }
}