package com.hanheldpos.ui.screens.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DropDownItem(
    val name: String,
    val position : Int,
    val realItem: Parcelable? = null,
) : Parcelable {

}