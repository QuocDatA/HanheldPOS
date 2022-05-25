package com.hanheldpos.prefs

object  PrefKey {
    const val TOKEN_EXPIRED_IN = "TOKEN_EXPIRED_IN"
    const val BEARER_TOKEN = "BEARER_TOKEN"
    const val SECRET_KEY = "SECRET_KEY"
    object Application {
        const val DARK_MODE = "DARK_MODE"
    }

    object Setting {
        const val UI = "UI"
        const val DEVICE_CODE = "DEVICE_CODE"
        const val RECENT_DEVICE_LIST = "RECENT_DEVICE_LIST"
    }

    object Order {
        const val MENU_RESP = "MENU_RESP"
        const val MENU_SETTING_RESP = "MENU_SETTING_RESP"
        const val MENU_STATUS_RESP = "MENU_STATUS_RESP"
        const val FILE_NAME_NUMBER_INCREASEMENT = "FILE_NAME_NUMBER_INCREASEMENT"
        const val ORDER_PENDING = "ORDER_PENDING"
        const val ORDER_COMPLETE = "ORDER_COMPLETE"
    }

    object Floor {
        const val FLOOR_RESP = "FLOOR_RESP"
    }

    object Fee {
        const val FEE_RESP = "FEE_RESP"
    }

    object Discount {
        const val DISCOUNT_RESP = "DISCOUNT_RESP"
        const val DISCOUNT_DETAIL_RESP = "DISCOUNT_DETAIL_RESP";
    }

    object Payment {
        const val PAYMENTS_RESP = "PAYMENTS_RESP"
    }

    object System {
        const val ADDRESS_TYPE = "ADDRESS_TYPE"
    }

    object Resource {
        const val RESOURCE_RESP = "RESOURCE_RESP"
    }
}