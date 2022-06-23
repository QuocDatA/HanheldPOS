package com.hanheldpos.ui.screens.menu.settings.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemBoxSettingOptionBinding
import com.hanheldpos.databinding.ItemRadioSettingOptionBinding
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class SettingsOptionAdapter(
    private val defaultSelection: Any? = null,
    private val style: SettingOptionType = SettingOptionType.RADIO,
    private val listener: BaseItemClickListener<ItemSettingOption>
) :
    BaseBindingListAdapter<ItemSettingOption>(DiffCallBack(), listener) {

    data class SelectedItem(var value: Int = -1)

    private var selectedItem: SelectedItem = SelectedItem(-1)

    override fun getItemViewType(position: Int): Int {
        return when (style) {
            SettingOptionType.RADIO -> R.layout.item_radio_setting_option
            SettingOptionType.BOX -> R.layout.item_box_setting_option
        }
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemSettingOption>,
        position: Int
    ) {
        val item = getItem(position)
        if (selectedItem.value == -1 && defaultSelection == item.value) {
            selectedItem.value = position
        }
        when (style) {
            SettingOptionType.RADIO -> (holder.binding as ItemRadioSettingOptionBinding).let { binding ->
                binding.item = item
                binding.btn.apply {
                    setOnClickListener {
                        notifyItemChanged(selectedItem.value)
                        selectedItem.value = position
                        notifyItemChanged(position)
                        listener.onItemClick(position, item)
                    }
                    isChecked = selectedItem.value == position
                }
            }
            SettingOptionType.BOX -> (holder.binding as ItemBoxSettingOptionBinding).let { binding ->
                binding.item = item
                binding.btn.apply {
                    setOnClickListener {
                        notifyItemChanged(selectedItem.value)
                        selectedItem.value = position
                        notifyItemChanged(position)
                        listener.onItemClick(position, item)
                    }
                    isChecked = selectedItem.value == position
                }
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ItemSettingOption>() {
        override fun areItemsTheSame(
            oldItem: ItemSettingOption,
            newItem: ItemSettingOption
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ItemSettingOption,
            newItem: ItemSettingOption
        ): Boolean {
            return oldItem == newItem
        }

    }
}