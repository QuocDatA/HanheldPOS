package com.hanheldpos.ui.screens.product.adapter.modifier

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.hanheldpos.data.api.pojo.order.ModifierItemItem

@Parcelize
data class ModifierSelectedItemModel(
    val realItem: ModifierItemItem?,

    val modifierAvatar: String? = null,

    var quantity: Int = 0,

    ) : Parcelable, Cloneable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}