package com.hanheldpos.ui.screens.menu.customers.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CustomerMenuAdapter(listener : BaseItemClickListener<CustomerResp>) : BaseBindingListAdapter<CustomerResp>(DiffCallBack(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_customer_menu
    }

    class DiffCallBack : DiffUtil.ItemCallback<CustomerResp>() {
        override fun areItemsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return oldItem == newItem
        }

    }
}