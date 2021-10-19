package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModifierStrProductModifier(

    @field:SerializedName("ModifierItem")
    val modifierItem: String? = null
) : Parcelable
