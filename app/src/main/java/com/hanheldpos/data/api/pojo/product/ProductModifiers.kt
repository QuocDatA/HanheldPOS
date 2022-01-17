package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModifiers(
    val MaximumModifier: Int,
    val ModifierGuid: String,
    val ModifierItemList: List<ModifierItem>,
    val ModifierOptionType: Int,
    val Name: String,
    val RequiredModifier: Int
) : Parcelable