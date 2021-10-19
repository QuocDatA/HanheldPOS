package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * This is the object that parse form the string in side the Product Object in OrderMenuResp
 */
@Parcelize
data class ModifierStrProduct(
    //todo(ModifierModal): add more field modifier here
    @field:SerializedName("ModifierGuid")
    val modifierGuid: String? = null,

    @field:SerializedName("ModifierName")
    val modifierName: String? = null
) : Parcelable {

    /**
     * @noteItem is the ProductModifierItem that link to
     */
    var nodeItem : Any? = null
}
