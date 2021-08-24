package com.hanheldpos.ui.screens.product.adapter

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductOptionExtra
import com.hanheldpos.databinding.ItemProductOptionExtraBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class ProductExtraOptionsAdapter(
    private val listener: BaseItemClickListener<ProductOptionExtra>
) : BaseBindingListAdapter<ProductOptionExtra>(DiffCallBack(), listener) {

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ProductOptionExtra>?) {
        selectedItem.value = 0
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_option_extra
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ProductOptionExtra>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val bindingHolder = holder.binding as ItemProductOptionExtraBinding

        //Set watcher to change of value in order to set up adjust state
        bindingHolder.txtExtraQuantity.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    try {
                        val result=text.toString().toInt()
                        checkExtraLimit(bindingHolder, result)
                    } catch (e: Exception) {

                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
        bindingHolder.btnMinusQuantity.setOnClickListener {
            bindingHolder.txtExtraQuantity.setText(
                (bindingHolder.txtExtraQuantity.text.toString().toInt() - 1).toString()
            )
        }
        bindingHolder.btnAddQuantity.setOnClickListener {
            bindingHolder.txtExtraQuantity.setText(
                (bindingHolder.txtExtraQuantity.text.toString().toInt() + 1).toString()
            )
        }
    }

    private fun checkExtraLimit(bindingHolder: ItemProductOptionExtraBinding, currentValue: Int) {

        //Set up the limitation states for extra value
        when {
            currentValue == 0 -> {
                bindingHolder.btnMinusQuantity.isClickable = false
                bindingHolder.btnMinusQuantity.setImageResource(R.drawable.ic_remove_disable)
            }
            currentValue > 10 -> {
                bindingHolder.btnAddQuantity.isClickable = false
                bindingHolder.btnAddQuantity.setImageResource(R.drawable.ic_add_disable)
            }
            else -> {
                bindingHolder.btnMinusQuantity.isClickable = true
                bindingHolder.btnMinusQuantity.setImageResource(R.drawable.ic_remove_adjust)
                bindingHolder.btnAddQuantity.isClickable = true
                bindingHolder.btnAddQuantity.setImageResource(R.drawable.ic_add_adjust)
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductOptionExtra>() {
        override fun areItemsTheSame(
            oldItem: ProductOptionExtra,
            newItem: ProductOptionExtra
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductOptionExtra,
            newItem: ProductOptionExtra
        ): Boolean {
            return oldItem == newItem
        }

    }
}