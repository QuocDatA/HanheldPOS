package com.hanheldpos.model.setting

import android.os.Parcelable
import com.hanheldpos.model.menu.settings.GeneralNotificationType
import com.hanheldpos.model.menu.settings.GeneralPushType
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeneralSetting(
    var notificationTime: GeneralNotificationType? = GeneralNotificationType.DEFAULT,
    var automaticallyPushOrdersTime: GeneralPushType? = GeneralPushType.MANUAL,
    var printingLanguage: String? = "en"
) : Parcelable