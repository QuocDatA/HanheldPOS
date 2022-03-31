package com.hanheldpos.ui.screens.discount.discount_detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDiscReqProductBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class RequirementProductAdapter : BaseBindingListAdapter<Any>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_disc_req_product
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Any>, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ItemDiscReqProductBinding
        when (item.javaClass) {
            String::class.java -> {
                binding.title = item.toString()
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return false
        }


    }
}