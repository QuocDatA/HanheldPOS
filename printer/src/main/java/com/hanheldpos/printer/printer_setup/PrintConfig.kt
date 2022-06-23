package com.hanheldpos.printer.printer_setup

import com.hanheldpos.printer.printer_setup.device_info.DeviceInfo
import com.hanheldpos.printer.printer_setup.device_info.DeviceType


class PrintConfig private constructor(
    val connectionType: PrinterConnectionTypes,
    val deviceType: DeviceType,
) {
    val deviceInfo: DeviceInfo = deviceType.deviceInfo()
    lateinit var lanConfig: LanConfig


    class LanConfig(
        val port: Int,
        val ipAddress: String = "192.168.0.87",
        val timeOut: Int = 30000
    ) {
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
        ): PrintConfig {
            val printConfig = PrintConfig(
                connectionType = PrinterConnectionTypes.LAN,
                deviceType = DeviceType.NO_SDK(deviceType)
            )

            printConfig.lanConfig = LanConfig(port, ipAddress, timeOut)

            return printConfig
        }

        fun bluetooth(
            deviceType: DeviceType.NO_SDK.Types
        ): PrintConfig {
            return PrintConfig(
                connectionType = PrinterConnectionTypes.BLUETOOTH,
                deviceType = DeviceType.NO_SDK(deviceType)
            )
        }

        fun urovo(): PrintConfig {
            return PrintConfig(
                connectionType = PrinterConnectionTypes.LAN,
                deviceType = DeviceType.SDK(DeviceType.SDK.Types.UROVO),
            )
        }
    }

    override fun toString(): String {
        return "Device Type: $deviceType, LAN Config: $lanConfig"
    }
}