package com.hanheldpos.ui.screens.home.order.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.databinding.ItemOrderProductBinding

import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class OrderProductAdapter(
    private val listener: BaseItemClickListener<ProductItem>
) : BaseBindingListAdapter<ProductItem>(DiffCallback(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ProductItem> {
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


    private class DiffCallback : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id == newItem.id && oldItem.id != null && newItem.id != null;
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return false;
        }

    }
}