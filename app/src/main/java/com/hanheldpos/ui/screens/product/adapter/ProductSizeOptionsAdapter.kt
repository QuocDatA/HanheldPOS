package com.hanheldpos.ui.screens.product.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.data.api.pojo.product.ProductOption
import com.hanheldpos.databinding.ItemOrderCategoryBinding
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.databinding.ItemProductOptionBinding
import com.hanheldpos.databinding.ItemProductOptionSizeBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class ProductSizeOptionsAdapter(
    private val listener: BaseItemClickListener<ProductOption>
) : BaseBindingListAdapter<ProductOption>(DiffCallBack(), listener) {

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ProductOption>?) {
        selectedItem.value = 0;
        super.submitList(list);
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_option_size;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductOption>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val bindingHolder = (holder.binding as ItemProductOptionSizeBinding)
        bindingHolder.radioBtnSelect.apply {
            setOnClickListener {
                this@ProductSizeOptionsAdapter.notifyItemChanged(selectedItem.value)
                selectedItem.value = position
                this@ProductSizeOptionsAdapter.notifyItemChanged(position)
                listener.onItemClick(position, item)
            }
            if (selectedItem.value == position) {
                isChecked = true
                bindingHolder.radioBtnSelect.setTextColor(Color.WHITE)
            } else {
                isChecked = false
                bindingHolder.radioBtnSelect.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grayBorder
                    )
                )
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductOption>() {
        override fun areItemsTheSame(oldItem: ProductOption, newItem: ProductOption): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: ProductOption, newItem: ProductOption): Boolean {
            return oldItem == newItem;
        }

    }
}