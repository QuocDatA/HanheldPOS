package com.hanheldpos.model.menu.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BillLanguageOption(val string: String) : Parcelable {
    ENGLISH("English"),
    VIETNAMESE("Vietnamese")
}