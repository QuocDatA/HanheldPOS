package com.hanheldpos.model.home.table

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableSummary(
    var _id: String,
    var TableName: String,
    var PeopleQuantity: Int,
) : Parcelable {
}