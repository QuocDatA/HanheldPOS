package com.hanheldpos.ui.screens.menu.settings

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.menu.ItemRadioSettingOption
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class RadioSettingsOptionAdapter(listener: BaseItemClickListener<ItemRadioSettingOption>) :
    BaseBindingListAdapter<ItemRadioSettingOption>(DiffCallBack(), listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_radio_setting_option
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ItemRadioSettingOption>() {
        override fun areItemsTheSame(
            oldItem: ItemRadioSettingOption,
            newItem: ItemRadioSettingOption
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: ItemRadioSettingOption,
            newItem: ItemRadioSettingOption
        ): Boolean {
            TODO("Not yet implemented")
        }

    }
}