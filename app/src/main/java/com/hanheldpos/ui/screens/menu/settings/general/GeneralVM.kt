package com.hanheldpos.ui.screens.menu.settings.general

import android.content.Context
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.model.menu.ItemRadioSettingOption
import com.hanheldpos.model.menu.settings.GeneralNotificationType
import com.hanheldpos.model.menu.settings.GeneralPushType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class GeneralVM : BaseUiViewModel<GeneralUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getNotificationOptions(context : Context): List<ItemRadioSettingOption> {
        return mutableListOf(
            ItemRadioSettingOption(
                title = context.getString(R.string.default_m),
                value = GeneralNotificationType.DEFAULT
            ),
            ItemRadioSettingOption(
                title = "1 Minute",
                value = GeneralNotificationType.TIME.apply { amount = 60 }
            ),
            ItemRadioSettingOption(
                title = "5 Minutes",
                value = GeneralNotificationType.TIME.apply { amount = 300 }
            ),
            ItemRadioSettingOption(
                title = context.getString(R.string.forever),
                value = GeneralNotificationType.FOREVER
            ),
        )
    }

    fun getPushOptions(context: Context) : List<ItemRadioSettingOption> {
        return mutableListOf(
            ItemRadioSettingOption(
                title = context.getString(R.string.manual),
                value = GeneralPushType.MANUAL
            ),
            ItemRadioSettingOption(
                title = "5 Minute",
                value = GeneralPushType.TIME.apply { amount = 300 }
            ),
            ItemRadioSettingOption(
                title = "15 Minutes",
                value = GeneralPushType.TIME.apply { amount = 15*60 }
            ),
            ItemRadioSettingOption(
                title = "30 Minutes",
                value = GeneralPushType.TIME.apply { amount = 30 * 60 }
            ),
            ItemRadioSettingOption(
                title = "1 Hour",
                value = GeneralPushType.TIME.apply { amount = 60 * 60 }
            ),
            ItemRadioSettingOption(
                title = "2 Hours",
                value = GeneralPushType.TIME.apply { amount = 120 * 60 }
            ),
        )
    }
}