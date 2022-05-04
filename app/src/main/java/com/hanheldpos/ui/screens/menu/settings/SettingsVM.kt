package com.hanheldpos.ui.screens.menu.settings

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.model.menu.settings.SettingsOptionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav

class SettingsVM  : BaseUiViewModel<SettingsUV>(){

    fun initSettingsItemList(context : Context) : List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                type = SettingsOptionType.GENERAL,
                name = getNameMenu(SettingsOptionType.GENERAL, context)
            ),
            ItemOptionNav(
                type = SettingsOptionType.HARDWARE,
                name = getNameMenu(SettingsOptionType.HARDWARE, context)
            ),
        )
    }

    private fun getNameMenu(type : SettingsOptionType, context: Context): String {
        return when(type){
            SettingsOptionType.GENERAL -> context.getString(R.string.general);
            SettingsOptionType.HARDWARE -> context.getString(R.string.hardware);
        }
    }

    fun onFragmentBackPress() {
        uiCallback?.onFragmentBackPressed()
    }
}