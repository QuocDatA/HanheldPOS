package com.hanheldpos.data.api.pojo.table

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.home.table.TableStatusType
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
data class TableResp(

        @field:SerializedName("Message")
        val message: String? = null,

        @field:SerializedName("PageSize")
        val pageSize: Int? = null,

        @field:SerializedName("PageCount")
        val pageCount: Int? = null,

        @field:SerializedName("PageNumber")
        val pageNumber: Int? = null,

        @field:SerializedName("Model")
        val model: List<ModelItem?>? = null,

        @field:SerializedName("ErrorMessage")
        val errorMessage: String? = null,

        @field:SerializedName("ItemsCount")
        val itemsCount: Int? = null,

        @field:SerializedName("DidError")
        val didError: Boolean? = null
) : Parcelable

@Parcelize
data class ResolutionListsItem(

        @field:SerializedName("ScaleL")
        val scaleL: Float? = null,

        @field:SerializedName("Visible")
        val visible: Int? = null,

        @field:SerializedName("ScaleT")
        val scaleT: Float? = null,

        @field:SerializedName("Padding")
        val padding: String? = null,

        @field:SerializedName("Height")
        val height: Float? = null,

        @field:SerializedName("Width")
        val width: Float? = null,

        @field:SerializedName("ScaleH")
        val scaleH: Float? = null,

        @field:SerializedName("ScaleW")
        val scaleW: Float? = null,

        @field:SerializedName("Resolution")
        val resolution: String? = null,

        @field:SerializedName("Name")
        val name: String? = null
) : Parcelable


@Parcelize
data class ModelItem(
        @field:SerializedName("Floor")
        val floor: List<FloorItem?>? = null,

        @field:SerializedName("TableType")
        val tableType: List<TableTypeItem?>? = null,

        @field:SerializedName("ResolutionLists")
        val resolutionLists: List<ResolutionListsItem>? = null,

        @field:SerializedName("TableStatus")
        val tableStatus: List<TableStatusItem?>? = null,

        @field:SerializedName("FloorTable")
        val floorTable: List<FloorTableItem?>? = null
) : Parcelable


@Parcelize
data class FloorTableItem(
        @field:SerializedName("_Id")
        val id: String? = null,

        @field:SerializedName("_rev")
        val rev: String? = null,

        @field:SerializedName("_key")
        val key: Int? = null,

        @field:SerializedName("FloorGuid")
        val floorGuid: String? = null,

        @field:SerializedName("TableTypeId")
        val tableTypeId: Int? = null,

        @field:SerializedName("SectionGuid")
        val sectionGuid: String? = null,

        @field:SerializedName("TableName")
        val tableName: String? = null,

        @field:SerializedName("PeopleQuantity")
        val peopleQuantity: String? = null,

        @field:SerializedName("Left")
        val left: Double? = null,

        @field:SerializedName("Top")
        val top: Double? = null,

        @field:SerializedName("Width")
        val width: Double? = null,

        @field:SerializedName("Height")
        val height: Double? = null,

        @field:SerializedName("CreateDate")
        val createDate: String? = null,

        @field:SerializedName("OrderNo")
        val orderNo: Int? = null,

        @field:SerializedName("Visible")
        var visible: Int? = null,

        @field:SerializedName("UserGuid")
        val userGuid: String? = null,
) : Parcelable {

        @kotlinx.parcelize.IgnoredOnParcel
        var uiType : TableModeViewType? = TableModeViewType.Table;

        @IgnoredOnParcel
        var tableStatus : TableStatusType =  TableStatusType.Available;

}

@Parcelize
data class TableStatusItem(

        @field:SerializedName("Title_en")
        val titleEn: String? = null,

        @field:SerializedName("Visible")
        val visible: Int? = null,

        @field:SerializedName("OrderNo")
        val orderNo: Int? = null,

        @field:SerializedName("Id")
        val id: Int? = null,

        @field:SerializedName("BgColor")
        val bgColor: String? = null,

        @field:SerializedName("Default")
        val default: Int? = null,

        @field:SerializedName("Title_vi")
        val titleVi: String? = null
) : Parcelable

@Parcelize
data class TableTypeItem(

        @field:SerializedName("Name_en")
        val nameEn: String? = null,

        @field:SerializedName("Acronymn")
        val acronymn: String? = null,

        @field:SerializedName("_rev")
        val rev: String? = null,

        @field:SerializedName("Visible")
        val visible: Int? = null,

        @field:SerializedName("TableTypeId")
        val tableTypeId: Int? = null,

        @field:SerializedName("Height")
        val height: Int? = null,

        @field:SerializedName("OrderNo")
        val orderNo: Int? = null,

        @field:SerializedName("_Id")
        val id: String? = null,

        @field:SerializedName("_key")
        val key: Int? = null,

        @field:SerializedName("Name_vi")
        val nameVi: String? = null,

        @field:SerializedName("Width")
        val width: Int? = null,

        @field:SerializedName("Handle")
        val handle: String? = null
) : Parcelable

@Parcelize
data class FloorItem(

        @field:SerializedName("FloorCode")
        val floorCode: String? = null,

        @field:SerializedName("Description")
        val description: String? = null,

        @field:SerializedName("UserGuid")
        val userGuid: String? = null,

        @field:SerializedName("_rev")
        val rev: String? = null,

        @field:SerializedName("FloorId")
        val floorId: Int? = null,

        @field:SerializedName("OrderNo")
        val orderNo: Int? = null,

        @field:SerializedName("_key")
        val key: Int? = null,

        @field:SerializedName("CreateDate")
        val createDate: String? = null,

        @field:SerializedName("Name")
        var name: String? = null,

        @field:SerializedName("Handle")
        val handle: String? = null,

        @field:SerializedName("LocationGuid")
        val locationGuid: String? = null,

        @field:SerializedName("Visible")
        val visible: Int? = null,

        @field:SerializedName("_Id")
        val id: String? = null
) : Parcelable
