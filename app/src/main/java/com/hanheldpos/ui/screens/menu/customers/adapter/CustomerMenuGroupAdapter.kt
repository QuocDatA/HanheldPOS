package com.hanheldpos.ui.screens.menu.customers.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.ItemCustomerMenuGroupBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CustomerMenuGroupAdapter(private val listener : BaseItemClickListener<CustomerResp>) : BaseBindingListAdapter<CustomerGroup>(DiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_customer_menu_group
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseBindingViewHolder<CustomerGroup>, position: Int) {

        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemCustomerMenuGroupBinding
        val adapter = CustomerMenuAdapter(listener)
        binding.customers.run {
            this.adapter = adapter
            adapter.submitList(item.customers)
            adapter.notifyDataSetChanged()
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<CustomerGroup>() {
        override fun areItemsTheSame(oldItem: CustomerGroup, newItem: CustomerGroup): Boolean {
            return oldItem.groupTitle == newItem.groupTitle
        }

        override fun areContentsTheSame(oldItem: CustomerGroup, newItem: CustomerGroup): Boolean {
            return oldItem == newItem
        }

    }
}