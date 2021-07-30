package com.diadiem.pos_config.utils.localization.core


//https://github.com/akexorcist/Localization
interface OnLocaleChangedListener {
    fun onBeforeLocaleChanged()

    fun onAfterLocaleChanged()
}
