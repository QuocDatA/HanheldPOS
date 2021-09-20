package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderSettingResp(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Model")
    val model: List<ModelItem?>? = null,

    @field:SerializedName("ErrorMessage")
    val errorMessage: String? = null,

    @field:SerializedName("DidError")
    val didError: Boolean? = null
) : Parcelable

@Parcelize
data class ListDiningOptionsItem(

    @field:SerializedName("GroupText")
    val groupText: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("GroupId")
    val groupId: Int? = null
) : Parcelable

@Parcelize
data class ModelItem(

    @field:SerializedName("ListVoid")
    val listVoid: List<ListVoidItem?>? = null,

    @field:SerializedName("ListDiningOptions")
    val listDiningOptions: List<ListDiningOptionsItem?>? = null,

    @field:SerializedName("ListComp")
    val listComp: List<ListCompItem?>? = null
) : Parcelable

@Parcelize
data class ListReasonsItem(

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null
) : Parcelable

@Parcelize
data class ListVoidItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("ListReasons")
    val listReasons: List<ListReasonsItem?>? = null
) : Parcelable

@Parcelize
data class ListCompItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("ListReasons")
    val listReasons: List<ListReasonsItem?>? = null
) : Parcelable
