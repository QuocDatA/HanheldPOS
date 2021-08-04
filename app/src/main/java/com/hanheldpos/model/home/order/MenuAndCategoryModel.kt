package com.hanheldpos.model.home.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MenuModel(
    @field:SerializedName("Active")
    val active: String? = null,

    @field:SerializedName("Acronymn")
    val acronymn: String? = null,

    @field:SerializedName("UrlSelected")
    val urlSelected: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("UrlNormal")
    val urlNormal: String? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null

) : Parcelable {
    @IgnoredOnParcel
    var selected: Boolean = false;
}

@Parcelize
data class CategoryModel(

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null,

    @field:SerializedName("Description")
    val description: String? = null,

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

) : Parcelable {


}

