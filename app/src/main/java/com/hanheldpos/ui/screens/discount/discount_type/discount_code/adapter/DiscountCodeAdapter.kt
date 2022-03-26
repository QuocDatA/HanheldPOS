package com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.ItemDiscountCodeBinding
import com.hanheldpos.databinding.ItemTabDiscountTypeBinding
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class DiscountCodeAdapter(private val listener : DiscountItemCallBack)  : BaseBindingListAdapter<DiscountResp>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_discount_code;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DiscountResp>, position: Int) {
        val item = getItem(position)

        holder.bindItem(item);
        val binding = holder.binding as ItemDiscountCodeBinding
        binding.btnViewDetail.setOnClickListener {
            listener.onViewDetailClick(item)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick()
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<DiscountResp>() {
        override fun areItemsTheSame(oldItem: DiscountResp, newItem: DiscountResp): Boolean {
            return oldItem == newItem;

        }

        override fun areContentsTheSame(oldItem: DiscountResp, newItem: DiscountResp): Boolean {
            return oldItem == newItem;
        }

    }

    interface DiscountItemCallBack {
        fun onViewDetailClick(item: DiscountResp)
        fun onItemClick()
    }
}