package com.hanheldpos.model.menu.report

class ReportItemDetail(
    val name : String,
    val qty : Int? = null,
    val amount : Double,
    val subValue : String? = null,
    val isBold : Boolean? = false,
    val isGray : Boolean? = false,
    val list: List<ReportItemDetail>? = mutableListOf(),
)