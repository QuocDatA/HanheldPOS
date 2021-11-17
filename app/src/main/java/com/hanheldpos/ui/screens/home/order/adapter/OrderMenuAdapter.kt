package com.hanheldpos.ui.screens.home.order.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderMenuBinding
import com.hanheldpos.databinding.ItemOrderMenuDirectionButtonBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderMenuAdapter(
    private val directionCallBack: Callback,
    private val listener: BaseItemClickListener<OrderMenuItem>
) : BaseBindingListAdapter<OrderMenuItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).uiType) {
            MenuModeViewType.Empty -> R.layout.item_order_menu_empty
            MenuModeViewType.Menu -> R.layout.item_order_menu
            else -> { // Note the block
                R.layout.item_order_menu_direction_button
            }
        }
    }


    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderMenuItem>,
        position: Int
    ) {
        val item = getItem(position);
        when (item.uiType) {
            MenuModeViewType.Menu -> {
                (holder.binding as ItemOrderMenuBinding).btnMain.setOnClickListener {
                    listener.onItemClick(position, item);
                }
            }
            MenuModeViewType.Empty -> {
            }
            else -> {
                (holder.binding as ItemOrderMenuDirectionButtonBinding).dirUp.setOnClickListener {
                    directionCallBack.directionSelectd(1)
                }
                (holder.binding as ItemOrderMenuDirectionButtonBinding).dirDown.setOnClickListener {
                    directionCallBack.directionSelectd(2)
                }
            }
        }
        holder.bindItem(item);

    }

    private class DiffCallBack : DiffUtil.ItemCallback<OrderMenuItem>() {
        override fun areItemsTheSame(
            oldItem: OrderMenuItem,
            newItem: OrderMenuItem
        ): Boolean {
            return oldItem.id.equals(newItem.id) && ((oldItem.uiType == newItem.uiType) || ( oldItem.uiType != MenuModeViewType.Menu && oldItem.uiType != MenuModeViewType.Empty && newItem.uiType != MenuModeViewType.Menu && newItem.uiType != MenuModeViewType.Empty))
        }

        override fun areContentsTheSame(
            oldItem: OrderMenuItem,
            newItem: OrderMenuItem
        ): Boolean {
            return oldItem == newItem && (oldItem.uiType == MenuModeViewType.Menu || oldItem.uiType == MenuModeViewType.Empty);
        }

    }

    interface Callback {
        fun directionSelectd(value: Int)
    }
}