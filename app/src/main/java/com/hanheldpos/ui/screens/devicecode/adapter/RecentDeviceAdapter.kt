package com.hanheldpos.ui.screens.devicecode.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.Device
import com.hanheldpos.databinding.ItemRecentAccountBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class RecentDeviceAdapter(private val listener : RecentAccountClickListener<Device>): BaseBindingListAdapter<Device>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_recent_account;
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Device>, position: Int) {
        val device = getItem(position)
        holder.bindItem(device)
        holder.binding.root.setOnClickListener {
            listener.onItemClick(position,device,holder.itemView);
        }
        (holder.binding as ItemRecentAccountBinding).btnDeleteAccount.setOnClickListener {
            listener.onDeleteClick(device)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Device>() {
        override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem.AppCode == newItem.AppCode;
        }

        override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem == newItem;
        }

    }

    interface RecentAccountClickListener<T> {
        fun onItemClick(adapterPosition: Int, item: T, view: View)
        fun onDeleteClick(item: T)
    }
}