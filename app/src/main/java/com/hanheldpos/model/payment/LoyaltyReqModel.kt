package com.hanheldpos.model.payment

data class LoyaltyReqModel(
    val CustomerGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
    val GrandTotal: Double?,
    val LocationGuid: String,
    val OrderGuid: String,
    val UserGuid: String
)