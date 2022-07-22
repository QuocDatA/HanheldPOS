package com.hanheldpos.ui.screens.menu.settings.hardware

import android.content.Context
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.settings.HardwareDeviceStatusType
import com.hanheldpos.model.menu.settings.HardwarePrinterDeviceType
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class HardwareVM : BaseUiViewModel<HardwareUV>() {

    private var printerDevices: MutableList<ItemSettingOption> = mutableListOf()
    private var otherDeviceSource: MutableList<ItemSettingOption> = mutableListOf()

    fun initData() {
        printerDevices = DataHelper.hardwareSettingLocalStorage?.printerList?.map {
            ItemSettingOption(it.name, it, connectionStatus = HardwarePrinterDeviceType.CONNECTING)
        }?.toMutableList() ?: mutableListOf()
        otherDeviceSource = mutableListOf(
            ItemSettingOption(
                "NFC",
                value = HardwareDeviceStatusType.NFC
            )
        )
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getPrinterStatusOptions(): List<ItemSettingOption> {
        return printerDevices
    }

    fun getDeviceStatusOptions(): List<ItemSettingOption> {
        return otherDeviceSource
    }

    fun getPrinterDeviceOptions(context: Context): List<ItemSettingOption> {
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

    fun updateDeviceStatus(printerId: String, status: HardwarePrinterDeviceType): Int {
        val printerIndex =
            printerDevices.indexOfFirst { (it.value as HardwarePrinter).id == printerId }

        if (printerIndex >= 0) {
            val currentPrinter =
                printerDevices[printerIndex]

            printerDevices[printerIndex] = currentPrinter.copy(connectionStatus = status)
        }

        return printerIndex
    }

}