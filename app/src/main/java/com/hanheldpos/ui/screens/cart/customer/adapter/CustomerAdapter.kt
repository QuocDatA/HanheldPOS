package com.hanheldpos.ui.screens.cart.customer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CustomerAdapter(private val listener : BaseItemClickListener<CustomerResp>) : BaseBindingListAdapter<CustomerResp>(DiffCallBack(),listener) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_customer;
    }

    class DiffCallBack : DiffUtil.ItemCallback<CustomerResp>() {
        override fun areItemsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return oldItem._Id == newItem._Id;
        }

        override fun areContentsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return  oldItem == newItem;
        }

    }
}