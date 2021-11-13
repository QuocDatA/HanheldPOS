package com.hanheldpos.ui.screens.home.order.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.model.home.order.ProductModelViewType
import com.hanheldpos.model.product.ProductOrderItem

import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderProductAdapter(
    private val listener: BaseItemClickListener<ProductOrderItem>
) : BaseBindingListAdapter<ProductOrderItem>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ProductOrderItem> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("OrderProductAdapter","RecycleView Height:" + parent.height);
            val height = ((parent.height) / 6 ) - parent.resources.getDimension(R.dimen._2sdp);
            val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height.toInt());
            (it as ItemOrderProductBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it, listener)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductOrderItem>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        if (item.uiType != ProductModelViewType.Empty){
            holder.binding.root.setOnClickListener { listener.onItemClick(position,item); }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductOrderItem>() {
        override fun areItemsTheSame(oldItem: ProductOrderItem, newItem: ProductOrderItem): Boolean {
            return oldItem.id.equals(newItem.id) && oldItem.uiType == newItem.uiType;
        }

        override fun areContentsTheSame(oldItem: ProductOrderItem, newItem: ProductOrderItem): Boolean {
            return  oldItem == newItem
        }

    }
}