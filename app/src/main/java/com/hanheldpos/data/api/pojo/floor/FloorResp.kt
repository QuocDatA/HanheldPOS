package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FloorResp(
    val Floor: List<Floor>,
    val FloorTable: List<FloorTable>,
    val ResolutionLists: List<ResolutionItem>,
    val TableStatus: List<TableStatusItem>,
    val TableType: List<TableTypeItem>,
) : Parcelable