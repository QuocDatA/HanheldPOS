package com.hanheldpos.model.product

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtraDoneModel(
    var selectedVariant: GroupItem? = null,
    var selectedModifiers: MutableList<ModifierSelectedItemModel>? = null,
    var diningOption: DiningOptionItem? = null
) : Parcelable, Cloneable {

    fun getVariantStr(separator: String): String? {
        var rs: String? = null

        selectedVariant?.let {
            rs = it.groupName?.replace(Const.SymBol.VariantSeparator, "$separator ")
        }

        return rs
    }


    fun getModifierStr(separator: String): String? {
        if (selectedModifiers == null) return null;
        val rs: MutableList<String> = mutableListOf()

        selectedModifiers?.forEach { entry ->
            val modifier: String =
                entry.realItem?.modifier + if (entry.quantity > 1) " (${entry.quantity})" else ""
            rs.add(modifier)
        }
        return rs.joinToString(separator = "$separator ");

    }
}



