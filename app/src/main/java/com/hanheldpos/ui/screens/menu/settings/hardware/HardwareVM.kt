package com.hanheldpos.ui.screens.menu.settings.hardware

import android.content.Context
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.settings.HardwareDeviceStatusType
import com.hanheldpos.model.menu.settings.HardwarePrinterDeviceType
import com.hanheldpos.model.menu.settings.HardwarePrinterStatusType
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class HardwareVM :BaseUiViewModel<HardwareUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getPrinterStatusOptions(context : Context) : List<ItemSettingOption>{
        return DataHelper.hardwareSettingLocalStorage?.printerList?.map {
            ItemSettingOption(it.name, it)
        }?: mutableListOf()
    }

    fun getDeviceStatusOptions(context : Context) : List<ItemSettingOption> {
        return mutableListOf(
            ItemSettingOption(
                "NFC",
                value = HardwareDeviceStatusType.NFC
            )
        )
    }

    fun getPrinterDeviceOptions(context : Context) : List<ItemSettingOption> {
        return mutableListOf(
            ItemSettingOption(
                "No Connection",
                value = HardwarePrinterDeviceType.NO_CONNECTION
            ),
            ItemSettingOption(
                "Connecting",
                value = HardwarePrinterDeviceType.CONNECTING
            ),
            ItemSettingOption(
                "Connected",
                value = HardwarePrinterDeviceType.CONNECTED
            )
        )
    }
}