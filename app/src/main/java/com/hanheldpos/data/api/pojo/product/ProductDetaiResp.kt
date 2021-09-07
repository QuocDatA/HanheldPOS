package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.example.pos2.repo.order.menu.ModifierStrProduct
import com.google.gson.annotations.SerializedName
import com.hanheldpos.data.api.pojo.order.GroupItem
import com.hanheldpos.data.api.pojo.order.ModifierItemItem
import com.hanheldpos.data.api.pojo.order.ProductModifierItem
import com.hanheldpos.data.api.pojo.order.VariantStrProduct

import com.hanheldpos.data.api.pojo.product.*
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


