package com.hanheldpos.ui.screens.cart.customer.add_customer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.ItemCustomerBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder


class CustomerAdapter(private val listener: CustomerAdapterCallback) :
    BaseBindingListAdapter<CustomerResp?>(
        DiffCallBack()
    ) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) R.layout.item_customer else R.layout.item_progress_loading
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CustomerResp?>, position: Int) {
        if (getItem(position) != null) {
            val item = getItem(position)
            holder.bindItem(item)
            val binding = holder.binding as ItemCustomerBinding
            binding.customerInfo.setOnClickDebounce {
                listener.onCustomerDetail(item)
            }
            binding.root.setOnClickDebounce {
                listener.onCustomerSelect(position, item)
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<CustomerResp?>() {
        override fun areItemsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return false
        }
    }

    interface CustomerAdapterCallback {
        fun onCustomerSelect(adapterPosition: Int, customer: CustomerResp?)
        fun onCustomerDetail(customer: CustomerResp?)
    }
}