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
import com.hanheldpos.databinding.ItemOrderProductBindingImpl
import com.hanheldpos.databinding.ItemOrderProductDirectionButtonBinding
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.ProductMenuItem

import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderProductAdapter(
    private val listener: BaseItemClickListener<ProductMenuItem>
) : BaseBindingListAdapter<ProductMenuItem>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).uiType){
            ProductModeViewType.Product -> R.layout.item_order_product;
            else->R.layout.item_order_product_direction_button;
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ProductMenuItem> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("OrderProductAdapter","RecycleView Height:" + parent.height);
            val height = ((parent.height) / 6 ) - parent.resources.getDimension(R.dimen._2sdp);
            val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height.toInt());
            if (it is ItemOrderProductBinding)
            (it as ItemOrderProductBinding).layoutMain.layoutParams = params;
            else (it as ItemOrderProductDirectionButtonBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it, listener);
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductMenuItem>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        if (item.uiType != ProductModeViewType.Empty){
            holder.binding.root.setOnClickListener { listener.onItemClick(position,item); }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductMenuItem>() {
        override fun areItemsTheSame(oldItem: ProductMenuItem, newItem: ProductMenuItem): Boolean {
            return oldItem.proOriginal?.id.equals(newItem.proOriginal?.id) && ((oldItem.uiType == newItem.uiType) || ( oldItem.uiType != ProductModeViewType.Product && oldItem.uiType != ProductModeViewType.Empty && newItem.uiType != ProductModeViewType.Product && newItem.uiType != ProductModeViewType.Empty))
        }

        override fun areContentsTheSame(oldItem: ProductMenuItem, newItem: ProductMenuItem): Boolean {
            return  false;
        }

    }
}