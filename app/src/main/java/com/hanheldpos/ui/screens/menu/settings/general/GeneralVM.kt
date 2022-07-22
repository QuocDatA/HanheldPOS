package com.hanheldpos.ui.screens.menu.settings.general

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.model.menu.settings.GeneralNotificationType
import com.hanheldpos.model.menu.settings.GeneralPushType
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class GeneralVM : BaseUiViewModel<GeneralUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getNotificationOptions(context: Context): List<ItemSettingOption> {
        return mutableListOf(
            ItemSettingOption(
                title = context.getString(R.string.default_m),
                value = GeneralNotificationType.DEFAULT
            ),
            ItemSettingOption(
                title = "1 Minute",
                value = GeneralNotificationType.TIME_1M
            ),
            ItemSettingOption(
                title = "5 Minutes",
                value = GeneralNotificationType.TIME_5M
            ),
            ItemSettingOption(
                title = context.getString(R.string.forever),
                value = GeneralNotificationType.FOREVER
            ),
        )
    }

    fun getPushOptions(context: Context): List<ItemSettingOption> {
        return mutableListOf(
            ItemSettingOption(
                title = context.getString(R.string.manual),
                value = GeneralPushType.MANUAL
            ),
            ItemSettingOption(
                title = "5 Minute",
                value = GeneralPushType.TIME_5M
            ),
            ItemSettingOption(
                title = "15 Minutes",
                value = GeneralPushType.TIME_15M
            ),
            ItemSettingOption(
                title = "30 Minutes",
                value = GeneralPushType.TIME_30M
            ),
            ItemSettingOption(
                title = "1 Hour",
                value = GeneralPushType.TIME_1H
            ),
            ItemSettingOption(
                title = "2 Hours",
                value = GeneralPushType.TIME_2H
            ),
        )
    }
}