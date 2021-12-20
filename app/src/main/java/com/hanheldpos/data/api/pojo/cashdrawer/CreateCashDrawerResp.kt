package com.hanheldpos.data.api.pojo.cashdrawer


data class CreateCashDrawerResp(
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Message: String?,
    val Model: List<CreateCashDrawer>
)