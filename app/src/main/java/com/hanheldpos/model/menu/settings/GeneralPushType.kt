package com.hanheldpos.model.menu.settings

enum class GeneralPushType(var value: Int? = null) {
    MANUAL(-1), TIME_5M(5 * 60), TIME_15M(15 * 60), TIME_30M(30 * 60), TIME_1H(60 * 60), TIME_2H(2 * 60 * 60);
}