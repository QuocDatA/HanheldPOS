package com.hanheldpos.ui.screens.cart.customer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener




class CustomerAdapter(private val listener : BaseItemClickListener<CustomerResp?>) : BaseBindingListAdapter<CustomerResp?>(DiffCallBack(),listener) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) R.layout.item_customer else R.layout.item_progress_loading;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CustomerResp?>, position: Int) {
        if (getItem(position) != null) {
            val item = getItem(position);
            holder.bindItem(item);
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<CustomerResp?>() {
        override fun areItemsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return oldItem._Id == newItem._Id;
        }

        override fun areContentsTheSame(oldItem: CustomerResp, newItem: CustomerResp): Boolean {
            return  false;
        }
    }
}