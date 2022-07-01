package com.hanheldpos.model.menu.settings

import android.graphics.Color
import com.hanheldpos.PosApp
import com.hanheldpos.R


data class ItemSettingOption(
    val title: String?,
    val value : Any,
    var connectionStatus : HardwarePrinterDeviceType? = HardwarePrinterDeviceType.NO_CONNECTION,
) {

}