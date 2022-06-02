package com.handheld.printer.printer_setup.device_info

import com.handheld.printer.printer_setup.device_info.DeviceInfo
import com.handheld.printer.printer_setup.device_info.DeviceType

class NoSDKDeviceInfo(private val deviceType: DeviceType.NO_SDK.Types) :
    DeviceInfo() {
    override fun paperWidth(): Float = when (deviceType) {
        DeviceType.NO_SDK.Types.POS -> 72f
        DeviceType.NO_SDK.Types.HANDHELD -> 48f
    }

    override fun charsPerLineNormal(): Int = when (deviceType) {
        DeviceType.NO_SDK.Types.POS -> 48
        DeviceType.NO_SDK.Types.HANDHELD -> 32
    }

    override fun charsPerLineLarge(): Int = when (deviceType) {
        DeviceType.NO_SDK.Types.POS -> 24
        DeviceType.NO_SDK.Types.HANDHELD -> 16
    }

    override fun leftColumnWidth(): Int = when (deviceType) {
        DeviceType.NO_SDK.Types.POS -> 6
        DeviceType.NO_SDK.Types.HANDHELD -> 4
    }

    override fun rightColumnWidth(): Int = 9

    override fun linesFeed(numLines: Int): Int = numLines

}