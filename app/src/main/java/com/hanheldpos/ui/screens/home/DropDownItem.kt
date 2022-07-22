package com.hanheldpos.ui.screens.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class DropDownItem(
    val name: String,
    val position : Int,
    val realItem: @RawValue Any? = null,
) : Parcelable