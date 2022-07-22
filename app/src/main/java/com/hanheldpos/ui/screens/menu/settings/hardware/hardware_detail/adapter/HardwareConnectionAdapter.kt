package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.adapter

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.databinding.ItemHardwareConnectionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter

class HardwareConnectionAdapter(

    private val listener: HardwareConnectionCallBack
) :
    BaseBindingListAdapter<HardwareConnection>(DiffCallBack()) {

    private var selectedItem: SettingsOptionAdapter.SelectedItem =
        SettingsOptionAdapter.SelectedItem(0)

    private var defaultSelectedPosition = -1
    private var textChange = ""

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_hardware_connection
    }

    override fun submitList(list: MutableList<HardwareConnection>?) {
        selectedItem.value = list?.indexOfFirst { it.isChecked == true } ?: 0
        defaultSelectedPosition = selectedItem.value
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<HardwareConnection>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemHardwareConnectionBinding
        binding.isSelected = (position == selectedItem.value)
        binding.btnRadio.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value)
                selectedItem.value = position
                notifyItemChanged(position)
                binding.isSelected = (position == selectedItem.value)
            }
            if (selectedItem.value == position) {
                checkStatusSave(
                    holder.absoluteAdapterPosition,
                    binding.editTextIPAddress.text.toString(),
                    item
                )
                //listener.onItemClick(position, item.apply { port = textChange })
            }
            isChecked = selectedItem.value == position

        }
        binding.editTextIPAddress.setText(item.port.toString())
        binding.editTextIPAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                checkStatusSave(
                    holder.absoluteAdapterPosition,
                    binding.editTextIPAddress.text.toString(),
                    item,
                )
            }
        })
    }

    fun checkStatusSave(position: Int, editorText: String, connection: HardwareConnection) {
        if ((selectedItem.value == defaultSelectedPosition && editorText.isNotBlank() && editorText != getItem(
                position
            ).port) || selectedItem.value != defaultSelectedPosition
        ) {
            listener.onItemClick(position, connection.apply { port = editorText })
            listener.onSaveEnable()
        } else {
            listener.onSaveDisable()
        }
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

    interface HardwareConnectionCallBack {
        fun onItemClick(position: Int, item: HardwareConnection)
        fun onSaveEnable()
        fun onSaveDisable()
    }
}