package com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class DiscountCodeAdapter(private val listener : BaseItemClickListener<DiscountItem>)  : BaseBindingListAdapter<DiscountItem>(DiffCallBack(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_discount_code;
    }



    private class DiffCallBack : DiffUtil.ItemCallback<DiscountItem>() {
        override fun areItemsTheSame(oldItem: DiscountItem, newItem: DiscountItem): Boolean {
            return oldItem == newItem;

        }

        override fun areContentsTheSame(oldItem: DiscountItem, newItem: DiscountItem): Boolean {
            return oldItem == newItem;
        }

    }
}