package com.hanheldpos.ui.screens.menu.settings.hardware

import android.content.Context
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
        return mutableListOf(
            ItemSettingOption(
                "Cashier",
                value = HardwarePrinterStatusType.CASHIER
            ),
            ItemSettingOption(
                "Pho",
                value = HardwarePrinterStatusType.PHO
            ),
            ItemSettingOption(
                "Expo",
                value = HardwarePrinterStatusType.EXPO
            ),
            ItemSettingOption(
                "Kitchen",
                value = HardwarePrinterStatusType.KITCHEN
            ),
        )
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
                "Bluetooth",
                value = HardwarePrinterDeviceType.BLUETOOTH
            ),
            ItemSettingOption(
                "Urovo",
                value = HardwarePrinterDeviceType.UROVO
            )
        )
    }
}