package com.hanheldpos.ui.screens.main.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderCategoryAdapter (
    private val listener : BaseItemClickListener<CategoryModel>
        ) : BaseBindingListAdapter<CategoryModel>(DiffCallBack(),listener) {


    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_category;
    }

    private class DiffCallBack : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem;
        }

    }
}