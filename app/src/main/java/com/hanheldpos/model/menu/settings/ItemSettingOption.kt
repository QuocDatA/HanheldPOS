package com.hanheldpos.model.menu.settings

import android.graphics.Color
import com.hanheldpos.PosApp
import com.hanheldpos.R


data class ItemSettingOption(
    val title: String?,
    val value : Any
) {
    fun getColor(): Int {
        return when(value as HardwarePrinterDeviceType) {
            HardwarePrinterDeviceType.NO_CONNECTION -> { PosApp.instance.getColor(R.color.color_5)
            }
            HardwarePrinterDeviceType.CONNECTING -> Color.YELLOW
            HardwarePrinterDeviceType.CONNECTED -> { PosApp.instance.getColor(R.color.color_0) }
        }
    }
}