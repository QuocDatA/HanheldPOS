package com.hanheldpos.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuModel(
    val id: String? = null,
    val name: String? = null,
    var selected: Boolean? = false,

    ) : Parcelable {

}
