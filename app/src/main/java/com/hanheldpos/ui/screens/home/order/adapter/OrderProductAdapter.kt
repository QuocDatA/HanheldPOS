package com.hanheldpos.ui.screens.home.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.databinding.ItemOrderProductDirectionButtonBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.CurCartData

class OrderProductAdapter(
    private val listener: BaseItemClickListener<ProductMenuItem>
) : BaseBindingListAdapter<ProductMenuItem>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).uiType) {
            ProductModeViewType.Product -> R.layout.item_order_product;
            else -> R.layout.item_order_product_direction_button;
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
            val height = ((parent.height) / 6) - parent.resources.getDimension(R.dimen._2sdp)
            if (it is ItemOrderProductBinding)
                it.layoutMain.layoutParams.height = height.toInt()
            else (it as ItemOrderProductDirectionButtonBinding).layoutMain.layoutParams.height =
                height.toInt()
            return BaseBindingViewHolder(it, listener)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductMenuItem>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)

        if (holder.binding is ItemOrderProductBinding) {
            item.proOriginal?.let {
                setPriceView(
                    holder.binding.priceProduct,
                    price = it.priceOverride(
                        CurCartData.cartModel?.menuLocationGuid,
                        it.skuDefault,
                        it.Price
                    )
                )
            }

        }



        if (item.uiType != ProductModeViewType.Empty) {

            holder.binding.root.setOnClickDebounce { listener.onItemClick(position, item); }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductMenuItem>() {
        override fun areItemsTheSame(oldItem: ProductMenuItem, newItem: ProductMenuItem): Boolean {
            return oldItem.proOriginal?._id.equals(newItem.proOriginal?._id) && ((oldItem.uiType == newItem.uiType) || (oldItem.uiType != ProductModeViewType.Product && oldItem.uiType != ProductModeViewType.Empty && newItem.uiType != ProductModeViewType.Product && newItem.uiType != ProductModeViewType.Empty))
        }

        override fun areContentsTheSame(
            oldItem: ProductMenuItem,
            newItem: ProductMenuItem
        ): Boolean {
            return oldItem == newItem && oldItem.uiType == newItem.uiType;
        }

    }
}