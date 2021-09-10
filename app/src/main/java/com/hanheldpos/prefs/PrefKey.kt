package com.hanheldpos.prefs

object PrefKey {
    const val TOKEN_EXPIRED_IN = "TOKEN_EXPIRED_IN"
    const val BEARER_TOKEN = "BEARER_TOKEN"

    object Application {
        const val DARK_MODE = "DARK_MODE"
    }

    object Setting {
        const val UI = "UI"
        const val DEVICE_CODE = "DEVICE_CODE"
    }

    object Order {
        const val ORDER_MENU_RESP = "ORDER_MENU_RESP"
        const val ORDER_SETTING_RESP = "ORDER_SETTING_RESP"
        const val FEE_RESP = "FEE_RESP"
    }

    object Table {
        const val TABLE_RESP = "TABLE_RESP"
    }
}