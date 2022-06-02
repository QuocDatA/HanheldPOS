package com.handheld.printer.printer_setup.device_info

abstract class DeviceInfo {
    fun printerDPI(): Int = 203

    abstract fun paperWidth(): Float
    abstract fun charsPerLineNormal(): Int
    abstract fun charsPerLineLarge(): Int

    // Number of characters per left column
    abstract fun leftColumnWidth(): Int

    // Number of characters per center column
    fun centerColumnWidth(): Int =
        charsPerLineNormal() - leftColumnWidth() - rightColumnWidth()

    // Number of characters per right column
    abstract fun rightColumnWidth(): Int

    abstract fun linesFeed(numLines: Int): Int

    companion object {
        fun sdk(deviceType: DeviceType.SDK.Types): SDKDeviceInfo =
            SDKDeviceInfo(deviceType)

        fun noSDK(deviceType: DeviceType.NO_SDK.Types) = NoSDKDeviceInfo(deviceType)
    }
}
