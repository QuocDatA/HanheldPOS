package com.hanheldpos.ui.screens.home.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceItem(
    val name: String? = null,
    val value: Double? = null,
) : Parcelable
