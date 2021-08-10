package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.model.home.order.type.OrderMenuModeViewType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderCategoryAdapter (
    private val mainType : OrderMenuModeViewType? = OrderMenuModeViewType.TextColor,
    private val listener : BaseItemClickListener<CategoryItem>
        ) : BaseBindingListAdapter<CategoryItem>(DiffCallBack(),listener) {


    override fun getItemViewType(position: Int): Int {

        return when(mainType){
            OrderMenuModeViewType.TextColor -> R.layout.item_order_category_color_style;
            OrderMenuModeViewType.TextImage -> R.layout.item_order_category_image_style;
            else -> R.layout.item_order_category_color_style;
        }

    }



    private class DiffCallBack : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

    }
}