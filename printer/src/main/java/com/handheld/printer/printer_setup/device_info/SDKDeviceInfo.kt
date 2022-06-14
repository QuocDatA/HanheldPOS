package com.handheld.printer.printer_setup.device_info

import com.handheld.printer.printer_setup.device_info.DeviceInfo
import com.handheld.printer.printer_setup.device_info.DeviceType

class SDKDeviceInfo(private val deviceType: DeviceType.SDK.Types) : DeviceInfo() {
    override fun paperWidth(): Float = 383f

    override fun charsPerLineNormal(): Int = 32

    override fun charsPerLineLarge(): Int = 18

    override fun leftColumnWidth(): Int = 4

    override fun rightColumnWidth(): Int = 9

    override fun linesFeed(numLines: Int): Int = 60 * numLines
}