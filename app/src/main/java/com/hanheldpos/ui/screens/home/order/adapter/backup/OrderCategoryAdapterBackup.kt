package com.hanheldpos.ui.screens.home.order.adapter.backup

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.CategoryItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderCategoryAdapterBackup(
    private val listener : BaseItemClickListener<CategoryItem>
) : BaseBindingListAdapter<CategoryItem>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_category_backup;
    }

    private class DiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id.equals(newItem.id);
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

    }
}