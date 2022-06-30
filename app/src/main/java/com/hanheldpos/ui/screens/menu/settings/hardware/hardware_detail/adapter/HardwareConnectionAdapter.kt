package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.adapter

import android.text.InputFilter
import android.text.Spanned
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.databinding.ItemHardwareConnectionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter

class HardwareConnectionAdapter(private val listener: BaseItemClickListener<HardwareConnection>) :
    BaseBindingListAdapter<HardwareConnection>(DiffCallBack()) {

    private var selectedItem: SettingsOptionAdapter.SelectedItem =
        SettingsOptionAdapter.SelectedItem(-1)

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_hardware_connection
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<HardwareConnection>,
        position: Int
    ) {
        val item = getItem(position)
        if (selectedItem.value == -1 && item.isChecked == true) {
            selectedItem.value = position
        }
        holder.bindItem(item)
        val binding = holder.binding as ItemHardwareConnectionBinding
        binding.isSelected = (position == selectedItem.value)
        binding.btnRadio.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value)
                selectedItem.value = position
                notifyItemChanged(position)
                binding.isSelected = (position == selectedItem.value)
                item.isChecked = !item.isChecked!!
                listener.onItemClick(position, item)
            }
            isChecked = selectedItem.value == position
        }
        binding.editTextIPAddress.hint = item.port.toString()
    }

    private class DiffCallBack : DiffUtil.ItemCallback<HardwareConnection>() {
        override fun areItemsTheSame(
            oldItem: HardwareConnection,
            newItem: HardwareConnection
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HardwareConnection,
            newItem: HardwareConnection
        ): Boolean {
            return oldItem == newItem
        }

    }
}