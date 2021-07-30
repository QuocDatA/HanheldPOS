package com.hanheldpos.ui.screens.home.order.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class OrderMenuAdapter(
    listener: BaseItemClickListener<MenuModel>
) : BaseBindingListAdapter<MenuModel>(DiffCallback(), listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_menu;
    }

    private class DiffCallback : DiffUtil.ItemCallback<MenuModel>() {
        override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
            return oldItem == newItem
        }

    }

}