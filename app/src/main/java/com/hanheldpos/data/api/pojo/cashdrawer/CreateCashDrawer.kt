package com.hanheldpos.data.api.pojo.cashdrawer

data class CreateCashDrawer(
    val CashDrawerGuid: String,
    val Code: String,
    val EndCash: Double,
    val StatusId: Int
)