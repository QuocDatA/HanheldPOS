package com.hanheldpos.model.menu.settings


data class ItemSettingOption(
    val title: String?,
    val value : Any,
    var connectionStatus : HardwarePrinterDeviceType? = HardwarePrinterDeviceType.NO_CONNECTION,
)