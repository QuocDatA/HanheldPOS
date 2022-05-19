package com.hanheldpos.ui.screens.home.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderMenuBinding
import com.hanheldpos.databinding.ItemOrderMenuDirectionButtonBinding
import com.hanheldpos.databinding.ItemOrderMenuEmptyBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.home.order.menu.MenuModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderMenuAdapter(
    private val listener: OrderMenuCallBack<OrderMenuItem>,
    private val baseListener: BaseItemClickListener<OrderMenuItem>
) : BaseBindingListAdapter<OrderMenuItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).uiType) {
            MenuModeViewType.Menu -> R.layout.item_order_menu
            MenuModeViewType.Empty -> R.layout.item_order_menu_empty
            else -> {
                R.layout.item_order_menu_direction_button
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<OrderMenuItem> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            val height = ((parent.height) / 13) - parent.resources.getDimension(R.dimen._2sdp)
            when (it) {
                is ItemOrderMenuBinding -> it.btnMain.layoutParams.height = height.toInt()
                is ItemOrderMenuDirectionButtonBinding -> it.layoutMain.layoutParams.height = height.toInt()
                else -> (it as ItemOrderMenuEmptyBinding).layoutMain.layoutParams.height = height.toInt()
            }
            return BaseBindingViewHolder(it, baseListener)
        }
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<OrderMenuItem>,
        position: Int
    ) {
        val item = getItem(position);
        when (item.uiType) {
            MenuModeViewType.Menu -> {
                (holder.binding as ItemOrderMenuBinding).btnMain.setOnClickDebounce {
                    baseListener.onItemClick(position, item);
                }
            }
            MenuModeViewType.Empty -> {
            }
            else -> {
                val view = (holder.binding as ItemOrderMenuDirectionButtonBinding)
                view.ivNextPage.setImageResource(R.drawable.ic_direction_down)
                view.ivPrevPage.setImageResource(R.drawable.ic_direction_up)
                when (item.uiType) {
                    MenuModeViewType.PrevButtonEnable -> {
                        view.ivNextPage.setImageResource(R.drawable.ic_direction_down_disable)
                        view.btnNextPage.isClickable = false
                        view.btnPrevPage.isClickable = true
                    }
                    MenuModeViewType.NextButtonEnable -> {
                        view.ivPrevPage.setImageResource(R.drawable.ic_direction_up_disable)
                        view.btnNextPage.isCheckable = true
                        view.btnPrevPage.isClickable = false
                    }
                    else -> {
                        view.ivPrevPage.setImageResource(R.drawable.ic_direction_up_disable)
                        view.ivNextPage.setImageResource(R.drawable.ic_direction_down_disable)
                        view.btnPrevPage.isClickable = false
                        view.btnNextPage.isClickable = false
                    }
                }
                view.btnPrevPage.setOnClickDebounce { listener.onBtnPrevClick() }
                view.btnNextPage.setOnClickDebounce { listener.onBtnNextClick() }
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

    interface OrderMenuCallBack<T> {
        fun onBtnPrevClick()
        fun onBtnNextClick()
    }
}