package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderMenuBinding
import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderMenuAdapter(
    private val directionCallBack: Callback,
    private val listener: BaseItemClickListener<OrderMenuItemModel>
) : BaseBindingListAdapter<OrderMenuItemModel>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_menu;
    }


    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderMenuItemModel>,
        position: Int
    ) {
        val item = getItem(position);
        when (item.uiType) {
            MenuModeViewType.Menu -> {
                (holder.binding as ItemOrderMenuBinding).btnMain.setOnClickListener {
                    listener.onItemClick(position, item);
                }
            }
            MenuModeViewType.DirectionButton -> {
                (holder.binding as ItemOrderMenuBinding).dirUp.setOnClickListener {
                    directionCallBack.directionSelectd(1)
                }
                (holder.binding as ItemOrderMenuBinding).dirDown.setOnClickListener {
                    directionCallBack.directionSelectd(2)
                }
            }
        }
        holder.bindItem(item);

    }

    private class DiffCallBack : DiffUtil.ItemCallback<OrderMenuItemModel>() {
        override fun areItemsTheSame(
            oldItem: OrderMenuItemModel,
            newItem: OrderMenuItemModel
        ): Boolean {
            return oldItem.id.equals(newItem.id) && oldItem.uiType == newItem.uiType;
        }

        override fun areContentsTheSame(
            oldItem: OrderMenuItemModel,
            newItem: OrderMenuItemModel
        ): Boolean {
            return oldItem == newItem;
        }

    }

    interface Callback {
        fun directionSelectd(value: Int)
    }
}