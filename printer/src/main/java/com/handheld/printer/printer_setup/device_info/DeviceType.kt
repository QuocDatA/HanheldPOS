package com.handheld.printer.printer_setup.device_info

sealed class DeviceType {
    class SDK(val type: Types) : DeviceType() {
        enum class Types {
            UROVO,
        }

        override fun deviceInfo(): DeviceInfo = DeviceInfo.sdk(type)

        override fun toString(): String {
            return type.name
        }
    }

    class NO_SDK(val type: Types) : DeviceType() {
        enum class Types {
            HANDHELD,
            POS,
        }

        override fun deviceInfo(): DeviceInfo = DeviceInfo.noSDK(type)

        override fun toString(): String = type.name
    }

    abstract fun deviceInfo(): DeviceInfo
}