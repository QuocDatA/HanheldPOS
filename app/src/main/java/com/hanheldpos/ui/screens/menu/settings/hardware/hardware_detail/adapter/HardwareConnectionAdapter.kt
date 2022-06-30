package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.adapter

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import androidx.core.graphics.blue
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.textfield.TextInputEditText
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.databinding.ItemHardwareConnectionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter

class HardwareConnectionAdapter(

    private val listener: HardwareConnectionCallBack
) :
    BaseBindingListAdapter<HardwareConnection>(DiffCallBack()) {

    private var selectedItem: SettingsOptionAdapter.SelectedItem =
        SettingsOptionAdapter.SelectedItem(0)

    private var defaultSelectedPosition = -1

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
                listener.onItemClick(position, item)
            }
            if (selectedItem.value == position) {
                checkStatusSave(holder.absoluteAdapterPosition, binding.editTextIPAddress)
                listener.onItemClick(position, item)
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
                checkStatusSave(holder.absoluteAdapterPosition, binding.editTextIPAddress)
            }
        })
    }

    fun checkStatusSave(position: Int, editor: TextInputEditText) {
        if ((selectedItem.value == defaultSelectedPosition && editor.text?.isNotBlank() == true && editor.text.toString() != getItem(
                position
            ).port) || selectedItem.value != defaultSelectedPosition
        ) {
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