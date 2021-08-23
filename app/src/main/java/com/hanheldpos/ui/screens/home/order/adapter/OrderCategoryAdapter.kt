package com.hanheldpos.ui.screens.home.order.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.CategoryItem
import com.hanheldpos.databinding.ItemOrderCategoryBinding
import com.hanheldpos.model.home.order.category.CategoryModeViewType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class OrderCategoryAdapter (
    private val directionCallBack : Callback,
    private val listener : BaseItemClickListener<CategoryItem>
        ) : BaseBindingListAdapter<CategoryItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_category;
    }


    override fun onBindViewHolder(holder: BaseBindingViewHolder<CategoryItem>, position: Int) {
        val item = getItem(position);
        when(item.uiType){
            CategoryModeViewType.Category->{
                (holder.binding as ItemOrderCategoryBinding).btnMain.setOnClickListener {
                    listener.onItemClick(position,item);
                }
            }
            CategoryModeViewType.DirectionButton->{
                (holder.binding as ItemOrderCategoryBinding).dirUp.setOnClickListener {
                    directionCallBack.directionSelectd(1)
                }
                (holder.binding as ItemOrderCategoryBinding).dirDown.setOnClickListener {
                    directionCallBack.directionSelectd(2)
                }
            }
        }
        holder.bindItem(item);

    }

    private class DiffCallBack : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id.equals(newItem.id) && oldItem.uiType == newItem.uiType;
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem;
        }

    }

    interface Callback{
        fun directionSelectd(value : Int)
    }
}