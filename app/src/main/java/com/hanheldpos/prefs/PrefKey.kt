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
    }

    object Order {
        const val MENU_RESP = "MENU_RESP"
        const val MENU_SETTING_RESP = "MENU_SETTING_RESP"
        const val FILE_NAME_NUMBER_INCREASEMENT = "FILE_NAME_NUMBER_INCREASEMENT"
    }

    object Table {
        const val TABLE_RESP = "TABLE_RESP"
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
}