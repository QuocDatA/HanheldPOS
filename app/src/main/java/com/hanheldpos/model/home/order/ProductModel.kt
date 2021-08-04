package com.hanheldpos.model.home.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val img: String? = null,
    val text: String? = null,
    val price: Double? = null,
    val description: String? = null,
    val selected: Boolean = false
) : Parcelable {

}

