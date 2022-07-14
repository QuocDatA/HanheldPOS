package com.hanheldpos.model.customer


import com.google.gson.annotations.SerializedName

data class ListGroupCustomerItem(
    @SerializedName("CustomerGuestGroupGuid")
    val customerGuestGroupGuid: String?,
    @SerializedName("GroupName")
    val groupName: String?
)