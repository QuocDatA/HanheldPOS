package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.menu.GroupItem

import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailResp(

    var selectedVariant: GroupItem? = null,

    var selectedModifierGroup: MutableMap<String, LinkedHashSet<ModifierSelectedItemModel>?>? = null,

    ) : Parcelable, Cloneable {

    public override fun clone(): ProductDetailResp {
        return copy()
    }


}


