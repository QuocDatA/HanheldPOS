package com.example.pos2.printer_setup

import com.example.pos2.printer_setup.device_info.DeviceInfo
import com.example.pos2.printer_setup.device_info.DeviceType

class PrintOptions private constructor(
    val connectionType: PrintConnectionType,
    val deviceType: DeviceType
) {
    val deviceInfo: DeviceInfo = deviceType.deviceInfo()
    lateinit var lanConfig: LanConfig


    class LanConfig(
        val port: Int,
        val ipAddress: String = "192.168.0.87",
        val timeOut: Int = 30000
    ){
        override fun toString(): String {
            return "IP: $ipAddress, port: $port"
        }
    }

    companion object {
        fun lan(
            port: Int = 9100,
            ipAddress: String = "192.168.0.87",
            timeOut: Int = 30000,
            deviceType: DeviceType.NO_SDK.Types
        ): PrintOptions {
            val printOptions = PrintOptions(
                connectionType = PrintConnectionType.LAN,
                deviceType = DeviceType.NO_SDK(deviceType)
            )

            printOptions.lanConfig = LanConfig(port, ipAddress, timeOut)

            return printOptions
        }

        fun bluetooth(
            deviceType: DeviceType.NO_SDK.Types
        ): PrintOptions {
            return PrintOptions(
                connectionType = PrintConnectionType.BLUETOOTH,
                deviceType = DeviceType.NO_SDK(deviceType)
            )
        }
    }

    override fun toString(): String {
        return "Device Type: $deviceType, LAN Config: $lanConfig"
    }
}