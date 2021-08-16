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
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.databinding.ItemOrderCategoryBinding
import com.hanheldpos.model.home.order.type.OrderMenuModeViewType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.home.order.OrderDataVM
import com.hanheldpos.ui.screens.home.order.OrderVM

class OrderCategoryAdapter (
    private val listener : BaseItemClickListener<CategoryItem>
        ) : BaseBindingListAdapter<CategoryItem>(DiffCallBack(),listener) {


    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<CategoryItem>?) {
        selectedItem.value = 0;
        super.submitList(list);


    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_category;
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<CategoryItem> {

        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("OrderNormalCategoryAdapter","RecycleView Height:" + parent.height);
            val height = ((parent.height) / OrderDataVM.maxItemViewCate ) - parent.resources.getDimension(R.dimen._2sdp);
            val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height.toInt());
            (it as ItemOrderCategoryBinding).text.layoutParams = params;
            return BaseBindingViewHolder(it, listener)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CategoryItem>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        (holder.binding as ItemOrderCategoryBinding).text.apply {
            setOnClickListener {
                this@OrderCategoryAdapter.notifyItemChanged(selectedItem.value);
                selectedItem.value = position;
                this@OrderCategoryAdapter.notifyItemChanged(position);
                listener.onItemClick(position,item);
            }
            background = when(selectedItem.value){
                position -> {
                    ContextCompat.getDrawable(holder.binding.root.context,R.drawable.bg_border_dart)
                }
                else -> null;
            }
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