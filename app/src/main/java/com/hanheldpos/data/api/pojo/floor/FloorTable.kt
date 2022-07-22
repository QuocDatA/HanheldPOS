package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.home.table.TableStatusType
import com.hanheldpos.model.order.OrderSummaryPrimary
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class FloorTable(
    val CreateDate: String,
    val FloorGuid: String,
    val Height: Double,
    val Left: Double,
    val OrderNo: Int,
    val PeopleQuantity: Int,
    val SectionGuid: String,
    val TableName: String,
    val TableTypeId: Int,
    val Top: Double,
    val UserGuid: String,
    var Visible: Int,
    val Width: Double,
    val _Id: String,
    val _key: Int,
    val _rev: String,
    var tableStatus : TableStatusType =  TableStatusType.Available,
    var orderSummary : OrderSummaryPrimary? = null
) : Parcelable {

    @IgnoredOnParcel
    var uiType : TableModeViewType = TableModeViewType.Table

    fun updateTableStatus(tableStatus : TableStatusType, orderSummary : OrderSummaryPrimary? = null) {
        this.tableStatus = tableStatus
        this.orderSummary = orderSummary
    }

}