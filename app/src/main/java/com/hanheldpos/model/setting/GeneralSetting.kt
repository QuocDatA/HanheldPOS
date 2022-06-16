package com.hanheldpos.model.setting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeneralSettingStorage(
    var notificationTime: Long? = -1,
    var automaticallyPushOrdersTime: Long? = -1,
    var printingLanguage: String? = "en"
) : Parcelable