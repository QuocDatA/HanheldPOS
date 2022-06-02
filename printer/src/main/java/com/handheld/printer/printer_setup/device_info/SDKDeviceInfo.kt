package com.example.pos2.printer_setup.device_info

class SDKDeviceInfo(private val deviceType: DeviceType.SDK.Types) : DeviceInfo() {
    override fun paperWidth(): Float = 383f

    override fun charsPerLineNormal(): Int = 32

    override fun charsPerLineLarge(): Int = 22

    override fun leftColumnWidth(): Int = 4

    override fun rightColumnWidth(): Int = 9

    override fun linesFeed(numLines: Int): Int = 60 * numLines
}