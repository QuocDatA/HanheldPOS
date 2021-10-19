package com.hanheldpos.ui.screens.product.adapter.modifier

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.hanheldpos.data.api.pojo.order.menu.ModifierItemItem

@Parcelize
data class ModifierSelectedItemModel(
    val realItem: ModifierItemItem?,
    val modifierAvatar: String? = null,
    val maxQuantity : Int = 1,
    var quantity: Int = 0,
    ) : Parcelable, Cloneable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun addQuantity(num : Int) {
        if(quantity < maxQuantity){
            quantity = quantity.plus(num);
        }
    }

    fun deleteQuantity(num: Int){
        if (quantity > 0) {
            quantity = quantity.minus(1);
        }
    }

    fun getSubtotalModifier() : Double {
        return realItem!!.price!!.times(quantity);
    }

}